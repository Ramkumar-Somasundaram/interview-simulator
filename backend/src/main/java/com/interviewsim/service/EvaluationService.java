package com.interviewsim.service;
 
import com.interviewsim.dto.*;
import com.interviewsim.exception.BadRequestException;
import com.interviewsim.exception.ResourceNotFoundException;
import com.interviewsim.model.*;
import com.interviewsim.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
 
@Service
public class EvaluationService {
 
    private final MockSessionRepository mockSessionRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserActivityRepository userActivityRepository;
    private final QuestionService questionService;
    private final DomainService domainService;
    private final TopicService topicService;
    private final UserRepository userRepository;
 
    public EvaluationService(
            MockSessionRepository mockSessionRepository,
            UserAnswerRepository userAnswerRepository,
            UserActivityRepository userActivityRepository,
            QuestionService questionService,
            DomainService domainService,
            TopicService topicService,
            UserRepository userRepository) {
 
        this.mockSessionRepository = mockSessionRepository;
        this.userAnswerRepository = userAnswerRepository;
        this.userActivityRepository = userActivityRepository;
        this.questionService = questionService;
        this.domainService = domainService;
        this.topicService = topicService;
        this.userRepository = userRepository;
    }
 
    // ================= SUBMIT ANSWERS =================
 
    @Transactional
    public EvaluationResponse submitAnswers(Long userId, SubmitAnswerRequest request) {
 
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
 
        Domain domain = domainService.findById(request.getDomainId());
        Topic topic = topicService.findById(request.getTopicId());
 
        if (!topic.getDomain().getId().equals(domain.getId())) {
            throw new BadRequestException("Topic does not belong to the specified domain");
        }
 
        int totalScore = 0;
        int maxPossibleScore = 0;
 
        List<EvaluationResponse.AnswerEvaluation> evaluations = new ArrayList<>();
 
        MockSession session = MockSession.builder()
                .user(user)
                .domain(domain)
                .topic(topic)
                .totalScore(0)
                .maxPossibleScore(0)
                .build();
 
        session = mockSessionRepository.save(session);
 
        for (SubmitAnswerRequest.SingleAnswerRequest ar : request.getAnswers()) {
 
            Question question = questionService.findById(ar.getQuestionId());
 
            if (!question.getTopic().getId().equals(topic.getId())) {
                throw new BadRequestException(
                        "Question " + ar.getQuestionId() + " does not belong to the selected topic");
            }
 
            int[] scoreAndMax = new int[2];
            List<String> matched = new ArrayList<>();
 
            evaluateAnswer(ar.getAnswerText(), question.getKeywords(), scoreAndMax, matched);
 
            int qScore = scoreAndMax[0];
            int qMax = scoreAndMax[1];
 
            totalScore += qScore;
            maxPossibleScore += qMax;
 
            // ✅ Auto generate suggestion
            String suggestion = generateSuggestion(qScore, qMax);
 
            // ✅ Save to DB with suggestion
            UserAnswer userAnswer = UserAnswer.builder()
                    .mockSession(session)
                    .question(question)
                    .answerText(ar.getAnswerText())
                    .score(qScore)
                    .matchedKeywords(String.join(",", matched))
                    .suggestion(suggestion)
                    .build();
 
            userAnswerRepository.save(userAnswer);
 
            // ✅ Add to response
            evaluations.add(EvaluationResponse.AnswerEvaluation.builder()
                    .questionId(question.getId())
                    .questionText(question.getQuestionText())
                    .userAnswer(ar.getAnswerText())
                    .score(qScore)
                    .maxScore(qMax)
                    .suggestion(suggestion)
                    .build());
        }
 
        session.setTotalScore(totalScore);
        session.setMaxPossibleScore(maxPossibleScore);
        mockSessionRepository.save(session);
 
        updateUserActivity(userId, user);
 
        double percentage = maxPossibleScore > 0
                ? Math.round((totalScore * 100.0 / maxPossibleScore) * 100.0) / 100.0
                : 0.0;
 
        return EvaluationResponse.builder()
                .sessionId(session.getId())
                .domainName(domain.getName())
                .topicName(topic.getName())
                .difficulty(topic.getDifficulty().name())
                .totalScore(totalScore)
                .maxPossibleScore(maxPossibleScore)
                .percentageScore(percentage)
                .attendedAt(session.getAttendedAt())
                .answerEvaluations(evaluations)
                .build();
    }
 
    // ================= SESSION PERFORMANCE =================
 
    @Transactional(readOnly = true)
    public EvaluationResponse getSessionPerformance(Long sessionId, Long userId) {
 
        MockSession session = mockSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
 
        if (!session.getUser().getId().equals(userId)) {
            throw new BadRequestException("Access denied to this session");
        }
 
        List<EvaluationResponse.AnswerEvaluation> evaluations =
                session.getUserAnswers().stream()
                        .map(ua -> {
 
                            int qMax = ua.getQuestion()
                                    .getKeywords()
                                    .stream()
                                    .mapToInt(Keyword::getRubricScore)
                                    .sum();
 
                            // ✅ BUG FIXED — use local variable, not ua.getSuggestion() directly
                            String suggestion = ua.getSuggestion();
                            if (suggestion == null || suggestion.isBlank()) {
                                suggestion = generateSuggestion(ua.getScore(), qMax);
                            }
 
                            return EvaluationResponse.AnswerEvaluation.builder()
                                    .questionId(ua.getQuestion().getId())
                                    .questionText(ua.getQuestion().getQuestionText())
                                    .userAnswer(ua.getAnswerText())
                                    .score(ua.getScore())
                                    .maxScore(qMax)
                                    .suggestion(suggestion) // ✅ FIXED — was ua.getSuggestion()
                                    .build();
                        })
                        .collect(Collectors.toList());
 
        double percentage = session.getMaxPossibleScore() > 0
                ? Math.round(
                (session.getTotalScore() * 100.0 / session.getMaxPossibleScore()) * 100.0
        ) / 100.0
                : 0.0;
 
        return EvaluationResponse.builder()
                .sessionId(session.getId())
                .domainName(session.getDomain().getName())
                .topicName(session.getTopic().getName())
                .difficulty(session.getTopic().getDifficulty().name())
                .totalScore(session.getTotalScore())
                .maxPossibleScore(session.getMaxPossibleScore())
                .percentageScore(percentage)
                .attendedAt(session.getAttendedAt())
                .answerEvaluations(evaluations)
                .build();
    }
 
    // ================= HELPERS =================
 
    private void evaluateAnswer(String answerText, List<Keyword> keywords,
                                int[] scoreAndMax, List<String> matched) {
 
        String lowerAnswer = (answerText == null || answerText.isBlank())
                ? ""
                : answerText.toLowerCase();
 
        int score = 0;
        int maxScore = 0;
 
        for (Keyword keyword : keywords) {
            maxScore += keyword.getRubricScore();
 
            if (!lowerAnswer.isEmpty()
                    && lowerAnswer.contains(keyword.getWord().toLowerCase())) {
                score += keyword.getRubricScore();
                matched.add(keyword.getWord());
            }
        }
 
        scoreAndMax[0] = score;
        scoreAndMax[1] = maxScore;
    }
 
    private String generateSuggestion(int score, int maxScore) {
 
        if (maxScore == 0) {
            return "No evaluation criteria defined for this question.";
        }
 
        double percentage = (score * 100.0) / maxScore;
 
        if (percentage == 0) {
            return "You need to study this concept from the beginning. "
                    + "Review the topic thoroughly and understand the core definition before attempting again.";
        } else if (percentage <= 40) {
            return "Your answer shows very limited understanding. "
                    + "Focus on the core definition, basic principles, and key terms of this concept.";
        } else if (percentage <= 70) {
            return "You have a basic understanding but missed important aspects. "
                    + "Try to cover more depth — think about types, real-world uses, and examples.";
        } else if (percentage < 100) {
            return "Good attempt! You are close to a complete answer. "
                    + "Try to be more comprehensive and cover all aspects of the concept.";
        } else {
            return "Excellent! You have demonstrated a strong and complete understanding of this concept.";
        }
    }
 
    private void updateUserActivity(Long userId, User user) {
 
        LocalDate today = LocalDate.now();
 
        Optional<UserActivity> existing =
                userActivityRepository.findByUserIdAndActivityDate(userId, today);
 
        if (existing.isPresent()) {
            UserActivity activity = existing.get();
            activity.setSubmissionCount(activity.getSubmissionCount() + 1);
            userActivityRepository.save(activity);
        } else {
            userActivityRepository.save(
                    UserActivity.builder()
                            .user(user)
                            .activityDate(today)
                            .submissionCount(1)
                            .build());
        }
    }
}
 
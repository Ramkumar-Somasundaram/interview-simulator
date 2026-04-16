package com.interviewsim.service;

import com.interviewsim.dto.*;
import com.interviewsim.exception.BadRequestException;
import com.interviewsim.exception.ResourceNotFoundException;
import com.interviewsim.model.*;
import com.interviewsim.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final KeywordRepository keywordRepository;
    private final TopicService topicService;

    public QuestionService(QuestionRepository questionRepository, KeywordRepository keywordRepository, TopicService topicService) {
        this.questionRepository = questionRepository;
        this.keywordRepository = keywordRepository;
        this.topicService = topicService;
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request) {
        Topic topic = topicService.findById(request.getTopicId());
        Question question = Question.builder().questionText(request.getQuestionText().trim()).topic(topic).build();
        question = questionRepository.save(question);
        Question finalQuestion = question;
        List<Keyword> keywords = request.getKeywords().stream()
                .map(kr -> Keyword.builder().word(kr.getWord().trim().toLowerCase())
                        .rubricScore(kr.getRubricScore()).question(finalQuestion).build())
                .collect(Collectors.toList());
        keywordRepository.saveAll(keywords);
        question.setKeywords(keywords);
        return mapToResponse(question);
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        Question question = findById(id);
        Topic topic = topicService.findById(request.getTopicId());
        question.setQuestionText(request.getQuestionText().trim());
        question.setTopic(topic);
        keywordRepository.deleteByQuestionId(id);
        List<Keyword> keywords = request.getKeywords().stream()
                .map(kr -> Keyword.builder().word(kr.getWord().trim().toLowerCase())
                        .rubricScore(kr.getRubricScore()).question(question).build())
                .collect(Collectors.toList());
        keywordRepository.saveAll(keywords);
        question.setKeywords(keywords);
        return mapToResponse(questionRepository.save(question));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        Question question = findById(id);
        questionRepository.delete(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsByTopic(Long topicId) {
        return questionRepository.findByTopicId(topicId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsForMock(Long domainId, Long topicId, Difficulty difficulty) {
        List<Question> questions = questionRepository.findByDomainTopicDifficulty(domainId, topicId, difficulty);
        if (questions.isEmpty()) {
            throw new BadRequestException("No questions available for the selected domain, topic, and difficulty");
        }
        return questions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
    }

    private QuestionResponse mapToResponse(Question question) {
        List<KeywordResponse> keywordResponses = question.getKeywords().stream()
                .map(k -> KeywordResponse.builder().id(k.getId()).word(k.getWord()).rubricScore(k.getRubricScore()).build())
                .collect(Collectors.toList());
        return QuestionResponse.builder().id(question.getId()).questionText(question.getQuestionText())
                .topicId(question.getTopic().getId()).topicName(question.getTopic().getName())
                .domainName(question.getTopic().getDomain().getName())
                .difficulty(question.getTopic().getDifficulty().name()).keywords(keywordResponses).build();
    }
}

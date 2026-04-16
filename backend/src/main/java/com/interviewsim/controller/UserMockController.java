package com.interviewsim.controller;

import com.interviewsim.dto.*;
import com.interviewsim.exception.ResourceNotFoundException;
import com.interviewsim.model.Difficulty;
import com.interviewsim.repository.UserRepository;
import com.interviewsim.service.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/mock")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserMockController {

    private final DomainService domainService;
    private final TopicService topicService;
    private final QuestionService questionService;
    private final EvaluationService evaluationService;
    private final UserRepository userRepository;

    public UserMockController(
            DomainService domainService,
            TopicService topicService,
            QuestionService questionService,
            EvaluationService evaluationService,
            UserRepository userRepository) {

        this.domainService = domainService;
        this.topicService = topicService;
        this.questionService = questionService;
        this.evaluationService = evaluationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/domains")
    public ResponseEntity<ApiResponse<List<DomainResponse>>> getDomains() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Domains fetched",
                        domainService.getDomainsWithQuestions()
                )
        );
    }

    /**
     * USER ONLY:
     * Topics with at least one question
     */
    @GetMapping("/domains/{domainId}/topics")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getTopics(
            @PathVariable Long domainId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Topics fetched",
                        topicService.getTopicsByDomainForUser(domainId)
                )
        );
    }

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestions(
            @RequestParam Long domainId,
            @RequestParam Long topicId,
            @RequestParam Difficulty difficulty) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Questions fetched",
                        questionService.getQuestionsForMock(domainId, topicId, difficulty)
                )
        );
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<EvaluationResponse>> submit(
            @Valid @RequestBody SubmitAnswerRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = resolveUserId(userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Evaluation complete",
                        evaluationService.submitAnswers(userId, request)
                )
        );
    }

    @GetMapping("/session/{sessionId}/performance")
    public ResponseEntity<ApiResponse<EvaluationResponse>> getPerformance(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = resolveUserId(userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Performance fetched",
                        evaluationService.getSessionPerformance(sessionId, userId)
                )
        );
    }

    private Long resolveUserId(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getId();
    }
}

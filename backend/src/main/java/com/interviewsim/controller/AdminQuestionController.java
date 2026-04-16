package com.interviewsim.controller;

import com.interviewsim.dto.*;
import com.interviewsim.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/questions")
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuestionController {

    private final QuestionService questionService;

    public AdminQuestionController(QuestionService questionService) { this.questionService = questionService; }

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse>> create(@Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Question created", questionService.createQuestion(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> update(@PathVariable Long id, @Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Question updated", questionService.updateQuestion(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success("Question deleted", null));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getByTopic(@PathVariable Long topicId) {
        return ResponseEntity.ok(ApiResponse.success("Questions fetched", questionService.getQuestionsByTopic(topicId)));
    }
}

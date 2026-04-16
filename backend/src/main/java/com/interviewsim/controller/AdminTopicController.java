package com.interviewsim.controller;

import com.interviewsim.dto.*;
import com.interviewsim.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/admin/topics")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTopicController {

    private final TopicService topicService;

    public AdminTopicController(TopicService topicService) { this.topicService = topicService; }

    @PostMapping
    public ResponseEntity<ApiResponse<TopicResponse>> create(@Valid @RequestBody TopicRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Topic created", topicService.createTopic(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TopicResponse>> update(@PathVariable Long id, @Valid @RequestBody TopicRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Topic updated", topicService.updateTopic(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok(ApiResponse.success("Topic deleted", null));
    }

    @GetMapping("/domain/{domainId}")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getByDomain(@PathVariable Long domainId) {
        return ResponseEntity.ok(ApiResponse.success("Topics fetched", topicService.getTopicsByDomain(domainId)));
    }
}

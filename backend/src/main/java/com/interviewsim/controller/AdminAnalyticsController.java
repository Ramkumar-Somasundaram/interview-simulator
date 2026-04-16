package com.interviewsim.controller;

import com.interviewsim.dto.ApiResponse;

import com.interviewsim.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin/analysis")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnalyticsController {

    private final AnalyticsService analyticsService;

    public AdminAnalyticsController(AnalyticsService analyticsService) { this.analyticsService = analyticsService; }

    @GetMapping("/most-attempted-domains")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostAttempted() {
        return ResponseEntity.ok(ApiResponse.success("Most attempted domains", analyticsService.getMostAttemptedDomains()));
    }
}

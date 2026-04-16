package com.interviewsim.controller;

import com.interviewsim.dto.*;
import com.interviewsim.exception.ResourceNotFoundException;
import com.interviewsim.repository.UserRepository;
import com.interviewsim.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/user/analytics")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserAnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;

    public UserAnalyticsController(AnalyticsService analyticsService, UserRepository userRepository) {
        this.analyticsService = analyticsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/heatmap")
    public ResponseEntity<ApiResponse<HeatmapResponse>> getHeatmap(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = resolveUserId(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Heatmap data fetched", analyticsService.getHeatmap(userId)));
    }

    @GetMapping("/trends")
    public ResponseEntity<ApiResponse<List<ScoreTrendResponse>>> getTrends(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = resolveUserId(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Score trends fetched", analyticsService.getScoreTrends(userId)));
    }

    private Long resolveUserId(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getId();
    }
}

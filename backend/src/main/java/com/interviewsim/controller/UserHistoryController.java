package com.interviewsim.controller;

import com.interviewsim.dto.*;
import com.interviewsim.exception.ResourceNotFoundException;
import com.interviewsim.repository.UserRepository;
import com.interviewsim.service.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/user/history")
@PreAuthorize("hasRole('USER')")
public class UserHistoryController {

    private final HistoryService historyService;
    private final UserRepository userRepository;

    public UserHistoryController(HistoryService historyService, UserRepository userRepository) {
        this.historyService = historyService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HistoryResponse>>> getHistory(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = resolveUserId(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("History fetched", historyService.getUserHistory(userId)));
    }

    private Long resolveUserId(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getId();
    }
}

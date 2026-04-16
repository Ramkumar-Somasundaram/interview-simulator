package com.interviewsim.service;

import com.interviewsim.dto.HistoryResponse;
import com.interviewsim.model.MockSession;
import com.interviewsim.repository.MockSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final MockSessionRepository mockSessionRepository;

    public HistoryService(MockSessionRepository mockSessionRepository) {
        this.mockSessionRepository = mockSessionRepository;
    }

    @Transactional(readOnly = true)
    public List<HistoryResponse> getUserHistory(Long userId) {
        return mockSessionRepository.findByUserIdOrderByAttendedAtDesc(userId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private HistoryResponse mapToResponse(MockSession session) {
        double pct = session.getMaxPossibleScore() > 0
                ? Math.round((session.getTotalScore() * 100.0 / session.getMaxPossibleScore()) * 100.0) / 100.0 : 0.0;
        return HistoryResponse.builder().sessionId(session.getId())
                .domainName(session.getDomain().getName()).topicName(session.getTopic().getName())
                .difficulty(session.getTopic().getDifficulty()).totalScore(session.getTotalScore())
                .maxPossibleScore(session.getMaxPossibleScore()).percentageScore(pct)
                .attendedAt(session.getAttendedAt()).build();
    }
}

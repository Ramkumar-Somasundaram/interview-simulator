package com.interviewsim.service;

import com.interviewsim.dto.*;
import com.interviewsim.model.MockSession;
import com.interviewsim.model.UserActivity;
import com.interviewsim.repository.MockSessionRepository;
import com.interviewsim.repository.UserActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final MockSessionRepository mockSessionRepository;
    private final UserActivityRepository userActivityRepository;

    public AnalyticsService(MockSessionRepository mockSessionRepository, UserActivityRepository userActivityRepository) {
        this.mockSessionRepository = mockSessionRepository;
        this.userActivityRepository = userActivityRepository;
    }

    @Transactional(readOnly = true)
    public HeatmapResponse getHeatmap(Long userId) {
        LocalDate from = LocalDate.now().minusDays(364);
        List<UserActivity> activities = userActivityRepository.findByUserIdAndActivityDateAfter(userId, from);
        Map<LocalDate, Integer> activityMap = activities.stream()
                .collect(Collectors.toMap(UserActivity::getActivityDate, UserActivity::getSubmissionCount));

        List<HeatmapResponse.DayActivity> dayActivities = new ArrayList<>();
        LocalDate cursor = from;
        LocalDate today = LocalDate.now();
        int totalSubmissions = 0;
        while (!cursor.isAfter(today)) {
            int count = activityMap.getOrDefault(cursor, 0);
            dayActivities.add(HeatmapResponse.DayActivity.builder().date(cursor).count(count).build());
            totalSubmissions += count;
            cursor = cursor.plusDays(1);
        }

        int currentStreak = calculateCurrentStreak(activityMap, today);
        int maxStreak = calculateMaxStreak(activityMap, from, today);
        long totalActiveDays = activityMap.values().stream().filter(c -> c > 0).count();

        return HeatmapResponse.builder().activities(dayActivities).totalActiveDays((int) totalActiveDays)
                .currentStreak(currentStreak).maxStreak(maxStreak).totalSubmissions(totalSubmissions).build();
    }

    @Transactional(readOnly = true)
    public List<ScoreTrendResponse> getScoreTrends(Long userId) {
        List<MockSession> sessions = mockSessionRepository.findByUserIdOrderByAttendedAtDesc(userId);
        Map<String, List<MockSession>> byDomain = sessions.stream()
                .collect(Collectors.groupingBy(s -> s.getDomain().getName()));
        return byDomain.entrySet().stream().map(entry -> {
            List<ScoreTrendResponse.TrendPoint> points = entry.getValue().stream()
                    .sorted(Comparator.comparing(MockSession::getAttendedAt))
                    .map(s -> {
                        double pct = s.getMaxPossibleScore() > 0
                                ? Math.round((s.getTotalScore() * 100.0 / s.getMaxPossibleScore()) * 100.0) / 100.0 : 0.0;
                        return ScoreTrendResponse.TrendPoint.builder().date(s.getAttendedAt())
                                .percentageScore(pct).topicName(s.getTopic().getName()).build();
                    }).collect(Collectors.toList());
            return ScoreTrendResponse.builder().domainName(entry.getKey()).points(points).build();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostAttemptedDomains() {
        List<Object[]> raw = mockSessionRepository.findMostAttemptedDomains();
        return raw.stream().map(row -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("domainName", row[0]);
            map.put("attemptCount", row[1]);
            return map;
        }).collect(Collectors.toList());
    }

    private int calculateCurrentStreak(Map<LocalDate, Integer> activityMap, LocalDate today) {
        int streak = 0;
        LocalDate cursor = today;
        while (activityMap.getOrDefault(cursor, 0) > 0) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    private int calculateMaxStreak(Map<LocalDate, Integer> activityMap, LocalDate from, LocalDate to) {
        int maxStreak = 0, currentStreak = 0;
        LocalDate cursor = from;
        while (!cursor.isAfter(to)) {
            if (activityMap.getOrDefault(cursor, 0) > 0) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 0;
            }
            cursor = cursor.plusDays(1);
        }
        return maxStreak;
    }
}

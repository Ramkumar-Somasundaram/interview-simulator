package com.interviewsim;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.interviewsim.dto.HeatmapResponse;
import com.interviewsim.dto.ScoreTrendResponse;
import com.interviewsim.model.Domain;
import com.interviewsim.model.MockSession;
import com.interviewsim.model.Topic;
import com.interviewsim.model.UserActivity;
import com.interviewsim.repository.MockSessionRepository;
import com.interviewsim.repository.UserActivityRepository;
import com.interviewsim.service.AnalyticsService;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private MockSessionRepository mockSessionRepository;

    @Mock
    private UserActivityRepository userActivityRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    /* ==========================================
       ✅ Test: getHeatmap
       ========================================== */
    @Test
    void getHeatmap_returnsCorrectStats() {
        Long userId = 1L;
        LocalDate today = LocalDate.now();

        UserActivity a1 = new UserActivity();
        a1.setActivityDate(today.minusDays(1));
        a1.setSubmissionCount(2);

        UserActivity a2 = new UserActivity();
        a2.setActivityDate(today);
        a2.setSubmissionCount(3);

        when(userActivityRepository.findByUserIdAndActivityDateAfter(eq(userId), any()))
                .thenReturn(List.of(a1, a2));

        HeatmapResponse response = analyticsService.getHeatmap(userId);

        assertNotNull(response);
        assertEquals(5, response.getTotalSubmissions());
        assertEquals(2, response.getTotalActiveDays());
        assertEquals(2, response.getCurrentStreak());
        assertTrue(response.getMaxStreak() >= 2);
        assertFalse(response.getActivities().isEmpty());
    }

    /* ==========================================
       ✅ Test: getScoreTrends
       ========================================== */
    @Test
    void getScoreTrends_groupsByDomainAndCalculatesPercentage() {
        Long userId = 1L;

        Domain domain = new Domain();
        domain.setName("Java");

        Topic topic = new Topic();
        topic.setName("Streams");

        MockSession session = new MockSession();
        session.setDomain(domain);
        session.setTopic(topic);
        session.setTotalScore(80);
        session.setMaxPossibleScore(100);
        

        when(mockSessionRepository.findByUserIdOrderByAttendedAtDesc(userId))
                .thenReturn(List.of(session));

        List<ScoreTrendResponse> trends = analyticsService.getScoreTrends(userId);

        assertEquals(1, trends.size());
        assertEquals("Java", trends.get(0).getDomainName());
        assertEquals(1, trends.get(0).getPoints().size());
        assertEquals(80.0, trends.get(0).getPoints().get(0).getPercentageScore());
    }

    /* ==========================================
       ✅ Test: getMostAttemptedDomains
       ========================================== */
    @Test
    void getMostAttemptedDomains_mapsRepositoryDataCorrectly() {
        when(mockSessionRepository.findMostAttemptedDomains())
                .thenReturn(List.of(
                        new Object[]{"Java", 10},
                        new Object[]{"Spring", 6}
                ));

        List<Map<String, Object>> result = analyticsService.getMostAttemptedDomains();

        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).get("domainName"));
        assertEquals(10, result.get(0).get("attemptCount"));
    }
}

package com.interviewsim.repository;

import com.interviewsim.model.MockSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MockSessionRepository extends JpaRepository<MockSession, Long> {
    List<MockSession> findByUserIdOrderByAttendedAtDesc(Long userId);

    @Query("SELECT ms FROM MockSession ms WHERE ms.user.id = :userId AND ms.domain.id = :domainId ORDER BY ms.attendedAt ASC")
    List<MockSession> findByUserIdAndDomainIdOrderByAttendedAt(
        @Param("userId") Long userId,
        @Param("domainId") Long domainId
    );

    @Query("SELECT ms.domain.name, COUNT(ms) as cnt FROM MockSession ms GROUP BY ms.domain.name ORDER BY cnt DESC")
    List<Object[]> findMostAttemptedDomains();
}

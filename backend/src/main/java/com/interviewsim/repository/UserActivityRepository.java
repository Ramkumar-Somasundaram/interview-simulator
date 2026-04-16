package com.interviewsim.repository;

import com.interviewsim.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    Optional<UserActivity> findByUserIdAndActivityDate(Long userId, LocalDate date);

    @Query("SELECT ua FROM UserActivity ua WHERE ua.user.id = :userId AND ua.activityDate >= :from ORDER BY ua.activityDate ASC")
    List<UserActivity> findByUserIdAndActivityDateAfter(
        @Param("userId") Long userId,
        @Param("from") LocalDate from
    );
}

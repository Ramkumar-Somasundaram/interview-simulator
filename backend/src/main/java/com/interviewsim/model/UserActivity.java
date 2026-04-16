package com.interviewsim.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_activity",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "activity_date"}))
public class UserActivity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Column(nullable = false)
    private Integer submissionCount;

    public UserActivity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getActivityDate() { return activityDate; }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }
    public Integer getSubmissionCount() { return submissionCount; }
    public void setSubmissionCount(Integer submissionCount) { this.submissionCount = submissionCount; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private User user; private LocalDate activityDate; private Integer submissionCount;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder activityDate(LocalDate activityDate) { this.activityDate = activityDate; return this; }
        public Builder submissionCount(Integer submissionCount) { this.submissionCount = submissionCount; return this; }
        public UserActivity build() {
            UserActivity ua = new UserActivity();
            ua.id = this.id; ua.user = this.user; ua.activityDate = this.activityDate; ua.submissionCount = this.submissionCount;
            return ua;
        }
    }
}

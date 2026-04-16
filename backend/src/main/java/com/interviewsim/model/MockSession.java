package com.interviewsim.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mock_sessions")
public class MockSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false)
    private Integer totalScore;

    @Column(nullable = false)
    private Integer maxPossibleScore;

    @Column(nullable = false, updatable = false)
    private LocalDateTime attendedAt;

    @OneToMany(mappedBy = "mockSession", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserAnswer> userAnswers = new ArrayList<>();

    @PrePersist
    protected void onCreate() { this.attendedAt = LocalDateTime.now(); }

    public MockSession() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Domain getDomain() { return domain; }
    public void setDomain(Domain domain) { this.domain = domain; }
    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public Integer getMaxPossibleScore() { return maxPossibleScore; }
    public void setMaxPossibleScore(Integer maxPossibleScore) { this.maxPossibleScore = maxPossibleScore; }
    public LocalDateTime getAttendedAt() { return attendedAt; }
    public void setAttendedAt(LocalDateTime attendedAt) { this.attendedAt = attendedAt; }
    public List<UserAnswer> getUserAnswers() { return userAnswers; }
    public void setUserAnswers(List<UserAnswer> userAnswers) { this.userAnswers = userAnswers; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private User user; private Domain domain; private Topic topic;
        private Integer totalScore = 0; private Integer maxPossibleScore = 0;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder domain(Domain domain) { this.domain = domain; return this; }
        public Builder topic(Topic topic) { this.topic = topic; return this; }
        public Builder totalScore(Integer totalScore) { this.totalScore = totalScore; return this; }
        public Builder maxPossibleScore(Integer maxPossibleScore) { this.maxPossibleScore = maxPossibleScore; return this; }
        public MockSession build() {
            MockSession ms = new MockSession();
            ms.id = this.id; ms.user = this.user; ms.domain = this.domain; ms.topic = this.topic;
            ms.totalScore = this.totalScore; ms.maxPossibleScore = this.maxPossibleScore;
            return ms;
        }
    }
}

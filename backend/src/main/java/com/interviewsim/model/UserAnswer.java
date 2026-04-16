package com.interviewsim.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_answers")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mock_session_id", nullable = false)
    private MockSession mockSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    @Column(nullable = false)
    private Integer score;

    // Optional – keep only if needed for future analytics
    @Column(columnDefinition = "TEXT")
    private String matchedKeywords;

    // ✅ PERFORMANCE FEEDBACK
    @Column(columnDefinition = "TEXT")
    private String suggestion;

    public UserAnswer() {}

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MockSession getMockSession() { return mockSession; }
    public void setMockSession(MockSession mockSession) { this.mockSession = mockSession; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getMatchedKeywords() { return matchedKeywords; }
    public void setMatchedKeywords(String matchedKeywords) {
        this.matchedKeywords = matchedKeywords;
    }

    // ✅ CORRECT ACCESSORS (THIS FIXES FRONTEND ISSUE)
    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    // ===== BUILDER =====

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private MockSession mockSession;
        private Question question;
        private String answerText;
        private Integer score;
        private String matchedKeywords;
        private String suggestion;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder mockSession(MockSession mockSession) {
            this.mockSession = mockSession;
            return this;
        }

        public Builder question(Question question) {
            this.question = question;
            return this;
        }

        public Builder answerText(String answerText) {
            this.answerText = answerText;
            return this;
        }

        public Builder score(Integer score) {
            this.score = score;
            return this;
        }

        public Builder matchedKeywords(String matchedKeywords) {
            this.matchedKeywords = matchedKeywords;
            return this;
        }

        public Builder suggestion(String suggestion) {
            this.suggestion = suggestion;
            return this;
        }

        public UserAnswer build() {
            UserAnswer ua = new UserAnswer();
            ua.id = this.id;
            ua.mockSession = this.mockSession;
            ua.question = this.question;
            ua.answerText = this.answerText;
            ua.score = this.score;
            ua.matchedKeywords = this.matchedKeywords;
            ua.suggestion = this.suggestion;
            return ua;
        }
    }
}
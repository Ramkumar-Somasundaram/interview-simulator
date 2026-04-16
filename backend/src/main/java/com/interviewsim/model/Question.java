package com.interviewsim.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Keyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy="question",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY)
    private List<UserAnswer> userAnswers =new ArrayList<>();
    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Question() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<Keyword> getKeywords() { return keywords; }
    public void setKeywords(List<Keyword> keywords) { this.keywords = keywords; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String questionText; private Topic topic;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder questionText(String questionText) { this.questionText = questionText; return this; }
        public Builder topic(Topic topic) { this.topic = topic; return this; }
        public Question build() {
            Question q = new Question();
            q.id = this.id; q.questionText = this.questionText; q.topic = this.topic;
            return q;
        }
    }
}

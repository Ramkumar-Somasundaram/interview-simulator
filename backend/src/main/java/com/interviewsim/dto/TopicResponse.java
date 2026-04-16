package com.interviewsim.dto;
import com.interviewsim.model.Difficulty;
import java.time.LocalDateTime;
public class TopicResponse {
    private Long id; private String name; private Difficulty difficulty;
    private Long domainId; private String domainName; private LocalDateTime createdAt; private int questionCount;
    public TopicResponse() {}
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public Difficulty getDifficulty() { return difficulty; } public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public Long getDomainId() { return domainId; } public void setDomainId(Long domainId) { this.domainId = domainId; }
    public String getDomainName() { return domainName; } public void setDomainName(String domainName) { this.domainName = domainName; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public int getQuestionCount() { return questionCount; } public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String name; private Difficulty difficulty; private Long domainId;
        private String domainName; private LocalDateTime createdAt; private int questionCount;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder difficulty(Difficulty difficulty) { this.difficulty = difficulty; return this; }
        public Builder domainId(Long domainId) { this.domainId = domainId; return this; }
        public Builder domainName(String domainName) { this.domainName = domainName; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder questionCount(int questionCount) { this.questionCount = questionCount; return this; }
        public TopicResponse build() {
            TopicResponse r = new TopicResponse();
            r.id = id; r.name = name; r.difficulty = difficulty; r.domainId = domainId;
            r.domainName = domainName; r.createdAt = createdAt; r.questionCount = questionCount;
            return r;
        }
    }
}

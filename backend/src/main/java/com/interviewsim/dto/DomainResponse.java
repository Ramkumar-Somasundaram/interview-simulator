package com.interviewsim.dto;
import java.time.LocalDateTime;
import java.util.List;
public class DomainResponse {
    private Long id; private String name; private String description;
    private LocalDateTime createdAt; private List<TopicResponse> topics; private boolean hasQuestions;
    public DomainResponse() {}
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<TopicResponse> getTopics() { return topics; } public void setTopics(List<TopicResponse> topics) { this.topics = topics; }
    public boolean isHasQuestions() { return hasQuestions; } public void setHasQuestions(boolean hasQuestions) { this.hasQuestions = hasQuestions; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String name; private String description;
        private LocalDateTime createdAt; private List<TopicResponse> topics; private boolean hasQuestions;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder topics(List<TopicResponse> topics) { this.topics = topics; return this; }
        public Builder hasQuestions(boolean hasQuestions) { this.hasQuestions = hasQuestions; return this; }
        public DomainResponse build() {
            DomainResponse r = new DomainResponse();
            r.id = id; r.name = name; r.description = description; r.createdAt = createdAt; r.topics = topics; r.hasQuestions = hasQuestions;
            return r;
        }
        
    }
}

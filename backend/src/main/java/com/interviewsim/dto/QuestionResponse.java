package com.interviewsim.dto;
import java.util.List;
public class QuestionResponse {
    private Long id; private String questionText; private Long topicId;
    private String topicName; private String domainName; private String difficulty; private List<KeywordResponse> keywords;
    public QuestionResponse() {}
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getQuestionText() { return questionText; } public void setQuestionText(String q) { this.questionText = q; }
    public Long getTopicId() { return topicId; } public void setTopicId(Long topicId) { this.topicId = topicId; }
    public String getTopicName() { return topicName; } public void setTopicName(String topicName) { this.topicName = topicName; }
    public String getDomainName() { return domainName; } public void setDomainName(String domainName) { this.domainName = domainName; }
    public String getDifficulty() { return difficulty; } public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public List<KeywordResponse> getKeywords() { return keywords; } public void setKeywords(List<KeywordResponse> keywords) { this.keywords = keywords; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String questionText; private Long topicId; private String topicName;
        private String domainName; private String difficulty; private List<KeywordResponse> keywords;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder questionText(String questionText) { this.questionText = questionText; return this; }
        public Builder topicId(Long topicId) { this.topicId = topicId; return this; }
        public Builder topicName(String topicName) { this.topicName = topicName; return this; }
        public Builder domainName(String domainName) { this.domainName = domainName; return this; }
        public Builder difficulty(String difficulty) { this.difficulty = difficulty; return this; }
        public Builder keywords(List<KeywordResponse> keywords) { this.keywords = keywords; return this; }
        public QuestionResponse build() {
            QuestionResponse r = new QuestionResponse();
            r.id = id; r.questionText = questionText; r.topicId = topicId; r.topicName = topicName;
            r.domainName = domainName; r.difficulty = difficulty; r.keywords = keywords;
            return r;
        }
    }
}

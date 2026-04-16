package com.interviewsim.dto;
import com.interviewsim.model.Difficulty;
import java.time.LocalDateTime;
public class HistoryResponse {
    private Long sessionId; private String domainName; private String topicName;
    private Difficulty difficulty; private Integer totalScore; private Integer maxPossibleScore;
    private Double percentageScore; private LocalDateTime attendedAt;
    public HistoryResponse() {}
    public Long getSessionId() { return sessionId; } public void setSessionId(Long v) { this.sessionId = v; }
    public String getDomainName() { return domainName; } public void setDomainName(String v) { this.domainName = v; }
    public String getTopicName() { return topicName; } public void setTopicName(String v) { this.topicName = v; }
    public Difficulty getDifficulty() { return difficulty; } public void setDifficulty(Difficulty v) { this.difficulty = v; }
    public Integer getTotalScore() { return totalScore; } public void setTotalScore(Integer v) { this.totalScore = v; }
    public Integer getMaxPossibleScore() { return maxPossibleScore; } public void setMaxPossibleScore(Integer v) { this.maxPossibleScore = v; }
    public Double getPercentageScore() { return percentageScore; } public void setPercentageScore(Double v) { this.percentageScore = v; }
    public LocalDateTime getAttendedAt() { return attendedAt; } public void setAttendedAt(LocalDateTime v) { this.attendedAt = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long sessionId; private String domainName; private String topicName; private Difficulty difficulty;
        private Integer totalScore; private Integer maxPossibleScore; private Double percentageScore; private LocalDateTime attendedAt;
        public Builder sessionId(Long v) { this.sessionId = v; return this; }
        public Builder domainName(String v) { this.domainName = v; return this; }
        public Builder topicName(String v) { this.topicName = v; return this; }
        public Builder difficulty(Difficulty v) { this.difficulty = v; return this; }
        public Builder totalScore(Integer v) { this.totalScore = v; return this; }
        public Builder maxPossibleScore(Integer v) { this.maxPossibleScore = v; return this; }
        public Builder percentageScore(Double v) { this.percentageScore = v; return this; }
        public Builder attendedAt(LocalDateTime v) { this.attendedAt = v; return this; }
        public HistoryResponse build() {
            HistoryResponse r = new HistoryResponse();
            r.sessionId = sessionId; r.domainName = domainName; r.topicName = topicName; r.difficulty = difficulty;
            r.totalScore = totalScore; r.maxPossibleScore = maxPossibleScore; r.percentageScore = percentageScore; r.attendedAt = attendedAt;
            return r;
        }
    }
}

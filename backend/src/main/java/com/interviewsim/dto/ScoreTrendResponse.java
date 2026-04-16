package com.interviewsim.dto;
import java.time.LocalDateTime;
import java.util.List;
public class ScoreTrendResponse {
    private String domainName; private List<TrendPoint> points;
    public ScoreTrendResponse() {}
    public String getDomainName() { return domainName; } public void setDomainName(String v) { this.domainName = v; }
    public List<TrendPoint> getPoints() { return points; } public void setPoints(List<TrendPoint> v) { this.points = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String domainName; private List<TrendPoint> points;
        public Builder domainName(String v) { this.domainName = v; return this; }
        public Builder points(List<TrendPoint> v) { this.points = v; return this; }
        public ScoreTrendResponse build() { ScoreTrendResponse r = new ScoreTrendResponse(); r.domainName = domainName; r.points = points; return r; }
    }
    public static class TrendPoint {
        private LocalDateTime date; private Double percentageScore; private String topicName;
        public TrendPoint() {}
        public LocalDateTime getDate() { return date; } public void setDate(LocalDateTime v) { this.date = v; }
        public Double getPercentageScore() { return percentageScore; } public void setPercentageScore(Double v) { this.percentageScore = v; }
        public String getTopicName() { return topicName; } public void setTopicName(String v) { this.topicName = v; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private LocalDateTime date; private Double percentageScore; private String topicName;
            public Builder date(LocalDateTime v) { this.date = v; return this; }
            public Builder percentageScore(Double v) { this.percentageScore = v; return this; }
            public Builder topicName(String v) { this.topicName = v; return this; }
            public TrendPoint build() { TrendPoint t = new TrendPoint(); t.date = date; t.percentageScore = percentageScore; t.topicName = topicName; return t; }
        }
    }
}

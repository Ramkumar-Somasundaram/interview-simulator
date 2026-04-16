package com.interviewsim.dto;
 
import java.time.LocalDateTime;
import java.util.List;
 
public class EvaluationResponse {
    private Long sessionId;
    private String domainName;
    private String topicName;
    private String difficulty;
    private Integer totalScore;
    private Integer maxPossibleScore;
    private Double percentageScore;
    private LocalDateTime attendedAt;
    private List<AnswerEvaluation> answerEvaluations;
 
    public EvaluationResponse() {}
 
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }
    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public Integer getMaxPossibleScore() { return maxPossibleScore; }
    public void setMaxPossibleScore(Integer maxPossibleScore) { this.maxPossibleScore = maxPossibleScore; }
    public Double getPercentageScore() { return percentageScore; }
    public void setPercentageScore(Double percentageScore) { this.percentageScore = percentageScore; }
    public LocalDateTime getAttendedAt() { return attendedAt; }
    public void setAttendedAt(LocalDateTime attendedAt) { this.attendedAt = attendedAt; }
    public List<AnswerEvaluation> getAnswerEvaluations() { return answerEvaluations; }
    public void setAnswerEvaluations(List<AnswerEvaluation> answerEvaluations) { this.answerEvaluations = answerEvaluations; }
 
    public static Builder builder() { return new Builder(); }
 
    public static class Builder {
        private Long sessionId;
        private String domainName;
        private String topicName;
        private String difficulty;
        private Integer totalScore;
        private Integer maxPossibleScore;
        private Double percentageScore;
        private LocalDateTime attendedAt;
        private List<AnswerEvaluation> answerEvaluations;
 
        public Builder sessionId(Long v) { this.sessionId = v; return this; }
        public Builder domainName(String v) { this.domainName = v; return this; }
        public Builder topicName(String v) { this.topicName = v; return this; }
        public Builder difficulty(String v) { this.difficulty = v; return this; }
        public Builder totalScore(Integer v) { this.totalScore = v; return this; }
        public Builder maxPossibleScore(Integer v) { this.maxPossibleScore = v; return this; }
        public Builder percentageScore(Double v) { this.percentageScore = v; return this; }
        public Builder attendedAt(LocalDateTime v) { this.attendedAt = v; return this; }
        public Builder answerEvaluations(List<AnswerEvaluation> v) { this.answerEvaluations = v; return this; }
 
        public EvaluationResponse build() {
            EvaluationResponse r = new EvaluationResponse();
            r.sessionId = sessionId;
            r.domainName = domainName;
            r.topicName = topicName;
            r.difficulty = difficulty;
            r.totalScore = totalScore;
            r.maxPossibleScore = maxPossibleScore;
            r.percentageScore = percentageScore;
            r.attendedAt = attendedAt;
            r.answerEvaluations = answerEvaluations;
            return r;
        }
    }
 
   
    public static class AnswerEvaluation {
        private Long questionId;
        private String questionText;
        private String userAnswer;
        private Integer score;
        private Integer maxScore;                
        private List<String> matchedKeywords;
       
        private String suggestion;             
 
        public AnswerEvaluation() {}
 
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }
        public String getUserAnswer() { return userAnswer; }
        public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
        public Integer getScore() { return score; }
        public void setScore(Integer score) { this.score = score; }
        public Integer getMaxScore() { return maxScore; }
        public void setMaxScore(Integer maxScore) { this.maxScore = maxScore; }
        public List<String> getMatchedKeywords() { return matchedKeywords; }
        public void setMatchedKeywords(List<String> matchedKeywords) { this.matchedKeywords = matchedKeywords; }
        public String getSuggestion() { return suggestion; }
        public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
 
        public static Builder builder() { return new Builder(); }
 
        public static class Builder {
            private Long questionId;
            private String questionText;
            private String userAnswer;
            private Integer score;
            private Integer maxScore;
            private List<String> matchedKeywords;
            private String suggestion;
 
            public Builder questionId(Long v) { this.questionId = v; return this; }
            public Builder questionText(String v) { this.questionText = v; return this; }
            public Builder userAnswer(String v) { this.userAnswer = v; return this; }
            public Builder score(Integer v) { this.score = v; return this; }
            public Builder maxScore(Integer v) { this.maxScore = v; return this; }
            public Builder matchedKeywords(List<String> v) { this.matchedKeywords = v; return this; }
            public Builder suggestion(String v) { this.suggestion = v; return this; }
 
            public AnswerEvaluation build() {
                AnswerEvaluation a = new AnswerEvaluation();
                a.questionId = questionId;
                a.questionText = questionText;
                a.userAnswer = userAnswer;
                a.score = score;
                a.maxScore = maxScore;
                a.matchedKeywords = matchedKeywords;
                a.suggestion = suggestion;
                return a;
            }
        }
    }
}
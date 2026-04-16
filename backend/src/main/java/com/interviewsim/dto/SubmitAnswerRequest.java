package com.interviewsim.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
public class SubmitAnswerRequest {
    @NotNull(message = "Domain ID must not be null") private Long domainId;
    @NotNull(message = "Topic ID must not be null") private Long topicId;
    @NotEmpty(message = "Answers list must not be empty") private List<SingleAnswerRequest> answers;
    public Long getDomainId() { return domainId; } public void setDomainId(Long domainId) { this.domainId = domainId; }
    public Long getTopicId() { return topicId; } public void setTopicId(Long topicId) { this.topicId = topicId; }
    public List<SingleAnswerRequest> getAnswers() { return answers; } public void setAnswers(List<SingleAnswerRequest> answers) { this.answers = answers; }

    public static class SingleAnswerRequest {
        @NotNull(message = "Question ID must not be null") private Long questionId;
        private String answerText;
        public Long getQuestionId() { return questionId; } public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getAnswerText() { return answerText; } public void setAnswerText(String answerText) { this.answerText = answerText; }
    }
}

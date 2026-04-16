package com.interviewsim.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
public class QuestionRequest {
    @NotBlank(message = "Question text must not be blank") private String questionText;
    @NotNull(message = "Topic ID must not be null") private Long topicId;
    @NotEmpty(message = "At least one keyword must be provided") private List<KeywordRequest> keywords;
    public String getQuestionText()
    { 
    	return questionText;
    } 
    public void setQuestionText(String questionText) 
    {
    	this.questionText = questionText; 
    }
    public Long getTopicId()
    { return topicId; } 
    public void setTopicId(Long topicId) 
    { this.topicId = topicId; }
    public List<KeywordRequest> getKeywords()
    { return keywords; } 
    public void setKeywords(List<KeywordRequest> keywords) { this.keywords = keywords; }
}

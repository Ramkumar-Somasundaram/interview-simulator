package com.interviewsim.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class KeywordRequest {
    @NotBlank(message = "Keyword word must not be blank") private String word;
    @NotNull(message = "Rubric score must not be null") @Min(value = 1, message = "Rubric score must be at least 1") private Integer rubricScore;
    public String getWord() { return word; } public void setWord(String word) { this.word = word; }
    public Integer getRubricScore() { return rubricScore; } public void setRubricScore(Integer rubricScore) { this.rubricScore = rubricScore; }
}

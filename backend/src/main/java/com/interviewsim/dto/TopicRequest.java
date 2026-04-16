package com.interviewsim.dto;
import com.interviewsim.model.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class TopicRequest {
    @NotBlank(message = "Topic name must not be blank") private String name;
    @NotNull(message = "Difficulty must not be null") private Difficulty difficulty;
    @NotNull(message = "Domain ID must not be null") private Long domainId;
    public String getName() { return name; } 
    public void setName(String name) { this.name = name; }
    public Difficulty getDifficulty() { return difficulty; } 
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public Long getDomainId() { return domainId; } 
    public void setDomainId(Long domainId) { this.domainId = domainId; }
}

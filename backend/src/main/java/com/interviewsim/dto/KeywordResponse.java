package com.interviewsim.dto;
public class KeywordResponse {
    private Long id; private String word; private Integer rubricScore;
    public KeywordResponse() {}
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getWord() { return word; } public void setWord(String word) { this.word = word; }
    public Integer getRubricScore() { return rubricScore; } public void setRubricScore(Integer rubricScore) { this.rubricScore = rubricScore; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String word; private Integer rubricScore;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder word(String word) { this.word = word; return this; }
        public Builder rubricScore(Integer rubricScore) { this.rubricScore = rubricScore; return this; }
        public KeywordResponse build() { KeywordResponse r = new KeywordResponse(); r.id = id; r.word = word; r.rubricScore = rubricScore; return r; }
    }
}

package com.interviewsim.model;

import jakarta.persistence.*;

@Entity
@Table(name = "keywords")
public class Keyword {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String word;

    @Column(nullable = false)
    private Integer rubricScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Keyword() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    public Integer getRubricScore() { return rubricScore; }
    public void setRubricScore(Integer rubricScore) { this.rubricScore = rubricScore; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String word; private Integer rubricScore; private Question question;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder word(String word) { this.word = word; return this; }
        public Builder rubricScore(Integer rubricScore) { this.rubricScore = rubricScore; return this; }
        public Builder question(Question question) { this.question = question; return this; }
        public Keyword build() {
            Keyword k = new Keyword();
            k.id = this.id; k.word = this.word; k.rubricScore = this.rubricScore; k.question = this.question;
            return k;
        }
    }
}

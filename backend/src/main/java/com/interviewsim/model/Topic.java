package com.interviewsim.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topics")
public class Topic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();
    
    @OneToMany(mappedBy="topic",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY)
     private List<MockSession>mockSessions=new ArrayList<>();

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Topic() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public Domain getDomain() { return domain; }
    public void setDomain(Domain domain) { this.domain = domain; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String name; private Difficulty difficulty; private Domain domain;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder difficulty(Difficulty difficulty) { this.difficulty = difficulty; return this; }
        public Builder domain(Domain domain) { this.domain = domain; return this; }
        public Topic build() {
            Topic t = new Topic();
            t.id = this.id; t.name = this.name; t.difficulty = this.difficulty; t.domain = this.domain;
            return t;
        }
    }
}

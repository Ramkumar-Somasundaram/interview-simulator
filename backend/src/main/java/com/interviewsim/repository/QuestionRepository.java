package com.interviewsim.repository;

import com.interviewsim.model.Difficulty;
import com.interviewsim.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopicId(Long topicId);

    @Query("SELECT q FROM Question q JOIN q.topic t WHERE t.domain.id = :domainId AND t.id = :topicId AND t.difficulty = :difficulty")
    List<Question> findByDomainTopicDifficulty(
        @Param("domainId") Long domainId,
        @Param("topicId") Long topicId,
        @Param("difficulty") Difficulty difficulty
    );
}

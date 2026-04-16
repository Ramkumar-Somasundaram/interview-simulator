package com.interviewsim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.interviewsim.model.Difficulty;
import com.interviewsim.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByDomainId(Long domainId);
    List<Topic> findByDomainIdAndDifficulty(Long domainId, Difficulty difficulty);
    boolean existsByNameIgnoreCaseAndDomainId(String name, Long domainId);

    @Query("""
           SELECT DISTINCT t
           FROM Topic t
           JOIN t.questions q
           WHERE t.domain.id = :domainId
       """)
       List<Topic> findTopicsWithQuestionsByDomainId(@Param("domainId") Long domainId);
   }



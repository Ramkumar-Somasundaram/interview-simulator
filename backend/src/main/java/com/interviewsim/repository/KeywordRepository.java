package com.interviewsim.repository;

import com.interviewsim.model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByQuestionId(Long questionId);
    void deleteByQuestionId(Long questionId);
}

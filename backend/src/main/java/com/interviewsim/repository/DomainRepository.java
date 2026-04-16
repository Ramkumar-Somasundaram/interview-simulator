package com.interviewsim.repository;

import com.interviewsim.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface DomainRepository extends JpaRepository<Domain, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Domain> findByNameIgnoreCase(String name);

    @Query("SELECT DISTINCT d FROM Domain d JOIN d.topics t JOIN t.questions q")
    List<Domain> findDomainsWithQuestions();
}

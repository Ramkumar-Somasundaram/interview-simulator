package com.interviewsim.service;

import com.interviewsim.dto.*;
import com.interviewsim.exception.DuplicateResourceException;
import com.interviewsim.exception.ResourceNotFoundException;
import com.interviewsim.model.Domain;
import com.interviewsim.model.Topic;
import com.interviewsim.repository.DomainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DomainService {

    private final DomainRepository domainRepository;

    public DomainService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    @Transactional
    public DomainResponse createDomain(DomainRequest request) {
        if (domainRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Domain '" + request.getName() + "' already exists");
        }
        Domain domain = Domain.builder().name(request.getName().trim()).description(request.getDescription()).build();
        return mapToResponse(domainRepository.save(domain));
    }

    @Transactional
    public DomainResponse updateDomain(Long id, DomainRequest request) {
        Domain domain = findById(id);
        domainRepository.findByNameIgnoreCase(request.getName().trim()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Domain name '" + request.getName() + "' already taken");
            }
        });
        domain.setName(request.getName().trim());
        domain.setDescription(request.getDescription());
        return mapToResponse(domainRepository.save(domain));
    }

    @Transactional
    public void deleteDomain(Long id) {
        Domain domain = findById(id);
        domainRepository.delete(domain);
    }

    @Transactional(readOnly = true)
    public List<DomainResponse> getAllDomains() {
        return domainRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DomainResponse> getDomainsWithQuestions() {
        return domainRepository.findDomainsWithQuestions().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public Domain findById(Long id) {
        return domainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found with id: " + id));
    }

    private DomainResponse mapToResponse(Domain domain) {
        boolean hasQuestions = domain.getTopics().stream().anyMatch(t -> !t.getQuestions().isEmpty());
        List<TopicResponse> topicResponses = domain.getTopics().stream()
                .map(t -> TopicResponse.builder().id(t.getId()).name(t.getName())
                        .difficulty(t.getDifficulty()).domainId(domain.getId())
                        .domainName(domain.getName()).createdAt(t.getCreatedAt())
                        .questionCount(t.getQuestions().size()).build())
                .collect(Collectors.toList());
        return DomainResponse.builder().id(domain.getId()).name(domain.getName())
                .description(domain.getDescription()).createdAt(domain.getCreatedAt())
                .topics(topicResponses).hasQuestions(hasQuestions).build();
    }
}

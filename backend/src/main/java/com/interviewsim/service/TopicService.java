package com.interviewsim.service;

import com.interviewsim.dto.*;
import com.interviewsim.exception.DuplicateResourceException;
import com.interviewsim.exception.ResourceNotFoundException;
import com.interviewsim.model.Domain;
import com.interviewsim.model.Topic;
import com.interviewsim.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final DomainService domainService;

    public TopicService(TopicRepository topicRepository, DomainService domainService) {
        this.topicRepository = topicRepository;
        this.domainService = domainService;
    }

  

    @Transactional
    public TopicResponse createTopic(TopicRequest request) {
        Domain domain = domainService.findById(request.getDomainId());

        if (topicRepository.existsByNameIgnoreCaseAndDomainId(
                request.getName().trim(), request.getDomainId())) {
            throw new DuplicateResourceException(
                    "Topic '" + request.getName() + "' already exists in this domain");
        }

        Topic topic = Topic.builder()
                .name(request.getName().trim())
                .difficulty(request.getDifficulty())
                .domain(domain)
                .build();

        return mapToResponse(topicRepository.save(topic));
    }

    @Transactional
    public TopicResponse updateTopic(Long id, TopicRequest request) {
        Topic topic = findById(id);
        Domain domain = domainService.findById(request.getDomainId());

        topicRepository.findByDomainId(request.getDomainId()).stream()
                .filter(t -> t.getName().equalsIgnoreCase(request.getName().trim())
                        && !t.getId().equals(id))
                .findFirst()
                .ifPresent(t -> {
                    throw new DuplicateResourceException(
                            "Topic '" + request.getName() + "' already exists in this domain");
                });

        topic.setName(request.getName().trim());
        topic.setDifficulty(request.getDifficulty());
        topic.setDomain(domain);

        return mapToResponse(topicRepository.save(topic));
    }

    @Transactional
    public void deleteTopic(Long id) {
        Topic topic = findById(id);
        topicRepository.delete(topic);
    }

    @Transactional(readOnly = true)
    public List<TopicResponse> getTopicsByDomain(Long domainId) {
        domainService.findById(domainId);
        return topicRepository.findByDomainId(domainId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

  
    @Transactional(readOnly = true)
    public List<TopicResponse> getTopicsByDomainForUser(Long domainId) {
        domainService.findById(domainId);

        return topicRepository.findTopicsWithQuestionsByDomainId(domainId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }



    public Topic findById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Topic not found with id: " + id));
    }

    private TopicResponse mapToResponse(Topic topic) {
        return TopicResponse.builder()
                .id(topic.getId())
                .name(topic.getName())
                .difficulty(topic.getDifficulty())
                .domainId(topic.getDomain().getId())
                .domainName(topic.getDomain().getName())
                .createdAt(topic.getCreatedAt())
                .questionCount(topic.getQuestions().size())
                .build();
    }
}

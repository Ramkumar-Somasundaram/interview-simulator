package com.interviewsim;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewsim.controller.AdminDomainController;
import com.interviewsim.dto.DomainRequest;
import com.interviewsim.dto.DomainResponse;
import com.interviewsim.exception.DuplicateResourceException;
import com.interviewsim.security.JwtAuthFilter;
import com.interviewsim.security.JwtUtils;
import com.interviewsim.security.UserDetailsServiceImpl;
import com.interviewsim.service.DomainService;

@WebMvcTest(AdminDomainController.class)
class AdminDomainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DomainService domainService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private DomainResponse mockDomain() {
        return DomainResponse.builder()
                .id(1L)
                .name("Java")
                .description("Java Questions")
                .createdAt(LocalDateTime.now())
                .topics(List.of())
                .hasQuestions(false)
                .build();
    }

    // ✅ ADMIN SUCCESS
    @Test
    @WithMockUser(roles = "ADMIN")
    void createDomain_validRequest_returns200() throws Exception {
        DomainRequest req = new DomainRequest();
        req.setName("Java");

        when(domainService.createDomain(any())).thenReturn(mockDomain());

        mockMvc.perform(post("/api/admin/domains")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

     
    @Test
    @WithMockUser(roles = "ADMIN")
    void createDomain_duplicate_returns409() throws Exception {
        DomainRequest req = new DomainRequest();
        req.setName("Java");

        when(domainService.createDomain(any()))
                .thenThrow(new DuplicateResourceException("Domain 'Java' already exists"));

        mockMvc.perform(post("/api/admin/domains")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }


    @Test
    @WithMockUser(roles = "USER")
    void createDomain_asUser_returns403() throws Exception {
        DomainRequest req = new DomainRequest();
        req.setName("Java");

        mockMvc.perform(post("/api/admin/domains")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    
    @Test
    void createDomain_noAuth_returns401() throws Exception {
        DomainRequest req = new DomainRequest();
        req.setName("Java");

        mockMvc.perform(post("/api/admin/domains")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
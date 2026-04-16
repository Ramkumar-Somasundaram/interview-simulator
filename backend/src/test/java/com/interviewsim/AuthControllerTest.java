package com.interviewsim;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewsim.controller.AuthController;
import com.interviewsim.dto.ApiResponse;
import com.interviewsim.dto.AuthResponse;
import com.interviewsim.dto.LoginRequest;
import com.interviewsim.dto.RegisterRequest;
import com.interviewsim.exception.DuplicateResourceException;
import com.interviewsim.security.JwtAuthFilter;
import com.interviewsim.security.JwtUtils;
import com.interviewsim.security.UserDetailsServiceImpl;
import com.interviewsim.service.AuthService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ IMPORTANT FIX
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    // ───────────────────── REGISTER TESTS ─────────────────────

    @Test
    void register_validRequest_returns200() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("testuser@gmail.com");
        req.setPassword("Password1");
        req.setUsername("TestUser");

        when(authService.register(any()))
                .thenReturn(ApiResponse.success("Registered successfully", null));

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Registered successfully"));
    }

    @Test
    void register_duplicateEmail_returns409() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("existing@gmail.com");
        req.setPassword("Password1");
        req.setUsername("TestUser");

        when(authService.register(any()))
                .thenThrow(new DuplicateResourceException(
                        "Email already registered. Please try a different email id"));

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Email already registered. Please try a different email id"));
    }

    @Test
    void register_invalidEmail_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("user@yahoo.com");
        req.setPassword("Password1");
        req.setUsername("TestUser");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.email").exists());
    }

    @Test
    void register_passwordTooShort_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("user@gmail.com");
        req.setPassword("Pass1");
        req.setUsername("TestUser");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.password").exists());
    }

    // ───────────────────── LOGIN TESTS ─────────────────────

    @Test
    void login_validUser_returns200() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("testuser@gmail.com");
        req.setPassword("Password1");

        AuthResponse response = AuthResponse.builder()
                .token("mock.jwt.token")
                .role("USER")
                .email("testuser@gmail.com")
                .username("TestUser")
                .userId(1L)
                .build();

        when(authService.login(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("mock.jwt.token"))
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    @Test
    void login_wrongPassword_returns401() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("testuser@gmail.com");
        req.setPassword("WrongPass1");

        when(authService.login(any()))
                .thenThrow(new org.springframework.security
                        .authentication.BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Invalid email or password"));
    }
}

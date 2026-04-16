package com.interviewsim;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewsim.security.JwtAuthFilter;
import com.interviewsim.security.JwtUtils;
import com.interviewsim.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
 
@AutoConfigureMockMvc
public abstract class BaseControllerTest {
 
    @Autowired
    protected MockMvc mockMvc;
 
    @Autowired
    protected ObjectMapper objectMapper;
 
    // Mock security components so no real auth chain fires
    @MockBean
    protected JwtUtils jwtUtils;
 
    @MockBean
    protected JwtAuthFilter jwtAuthFilter;
 
    @MockBean
    protected UserDetailsServiceImpl userDetailsService;
}
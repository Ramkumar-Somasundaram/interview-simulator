package com.interviewsim.dto;

public class AuthResponse {
    private String token;
    private String role;
    private String email;
    private String username;
    private Long userId;

    public AuthResponse() {}
    public AuthResponse(String token, String role, String email, String username, Long userId) {
        this.token = token; this.role = role; this.email = email; this.username = username; this.userId = userId;
    }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String token; private String role; private String email; private String username; private Long userId;
        public Builder token(String token) { this.token = token; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public AuthResponse build() { return new AuthResponse(token, role, email, username, userId); }
    }
}

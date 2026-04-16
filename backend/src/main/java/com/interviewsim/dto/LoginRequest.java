package com.interviewsim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+\\-]+@gmail\\.com$", message = "Email is invalid. Must be a valid @gmail.com address")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

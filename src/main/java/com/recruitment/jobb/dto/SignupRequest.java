package com.recruitment.jobb.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class SignupRequest {
    @NotBlank(message="Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message="Invalid emial format")
    private String email;

    @NotBlank(message="Password is required")
    private String password;

    private String profileHeaderline;

    @NotBlank(message="Address is required")
    private String address;
}

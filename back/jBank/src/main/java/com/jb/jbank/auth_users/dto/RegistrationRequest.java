package com.jb.jbank.auth_users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "First name is required")
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email
    private String email;
    private List<String> roles;
    @NotBlank(message = "Password is required")
    private String password;


}
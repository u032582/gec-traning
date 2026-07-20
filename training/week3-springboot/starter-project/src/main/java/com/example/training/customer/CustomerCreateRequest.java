package com.example.training.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(
        @NotBlank
        @Size(max = 100)
        String name,
        @NotBlank
        @Email
        String email
) {}
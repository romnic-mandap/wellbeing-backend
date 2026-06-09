package com.example.meal2.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreationDTO(
        @NotBlank(message="username => must not be blank")
        @Pattern(regexp="^[A-Za-z0-9]*$", message="username => must be alpha/numeric only")
        @Size(min=6, max=64, message="username => must be between 6 and 64 characters")
        String username,
        @NotBlank(message="password => must not be blank")
        @Size(min=6, max=128, message="password => must be between 6 and 128 characters")
        String password,
        @NotBlank(message="passwordConfirmation => must not be blank")
        @Size(min=6, max=128, message="password => must be between 6 and 128 characters")
        String passwordConfirmation
) {
}

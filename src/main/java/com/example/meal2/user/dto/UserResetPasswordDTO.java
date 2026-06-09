package com.example.meal2.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserResetPasswordDTO(
        @NotBlank(message="password => must not be blank")
        String oldPassword,
        @NotBlank(message="password => must not be blank")
        @Size(min=6, max=128, message="password => must be between 6 and 128 characters")
        String password,
        @NotBlank(message="passwordConfirmation => must not be blank")
        @Size(min=6, max=128, message="password => must be between 6 and 128 characters")
        String passwordConfirmation
) {
}

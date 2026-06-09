package com.example.meal2.user.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminResetUserPasswordDTO(
        @NotBlank(message="username => must not be blank")
        String username
) {
}

package com.example.meal2.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ToDoCreationDTO(
        @NotBlank(message="description => must not be blank")
        @Size(max=255, message="description => must not exceed 255 characters")
        String description
) {
}

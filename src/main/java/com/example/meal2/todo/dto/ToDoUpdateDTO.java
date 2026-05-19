package com.example.meal2.todo.dto;

import jakarta.validation.constraints.NotNull;

public record ToDoUpdateDTO(
        Long id,
        @NotNull(message="isDone => must not be blank")
        Boolean isDone
) {
}

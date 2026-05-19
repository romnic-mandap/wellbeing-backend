package com.example.meal2.todo.dto;

public record ToDoDTO(
        Long id,
        Integer userId,
        String description,
        Boolean isDone
) {
}

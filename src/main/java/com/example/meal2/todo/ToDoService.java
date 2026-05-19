package com.example.meal2.todo;

import com.example.meal2.todo.dto.ToDoCreationDTO;
import com.example.meal2.todo.dto.ToDoDTO;
import com.example.meal2.todo.dto.ToDoUpdateDTO;
import com.example.meal2.user.User;

import java.util.List;

public interface ToDoService {
    ToDoDTO addToDo(User user, ToDoCreationDTO toDoCreationDTO);
    ToDoDTO getToDo(User user, Long toDoId);
    List<ToDoDTO> getUserToDos(User user);
    void deleteToDo(User user, Long toDoId);
    void deleteUserToDos(User user);
    ToDoDTO updateToDo(User user, ToDoUpdateDTO toDoUpdateDTO);
    Integer countUserToDos(User user);
}

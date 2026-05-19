package com.example.meal2.todo;

import com.example.meal2.todo.dto.ToDoCreationDTO;
import com.example.meal2.todo.dto.ToDoDTO;
import com.example.meal2.todo.dto.ToDoUpdateDTO;
import com.example.meal2.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ToDoController {

    private final String PATH_HEADER = "/to-dos";

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @PostMapping(value=PATH_HEADER, produces={"application/json"}, consumes={"application/json"})
    public ResponseEntity<ToDoDTO> createToDo(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ToDoCreationDTO toDoCreationDTO
            ){
        return new ResponseEntity<>(
                toDoService.addToDo(user, toDoCreationDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping(value=PATH_HEADER, produces={"application/json"})
    public ResponseEntity<List<ToDoDTO>> getToDos(
            @AuthenticationPrincipal User user
    ){
        return new ResponseEntity<>(
                toDoService.getUserToDos(user),
                HttpStatus.OK
        );
    }

    @GetMapping(value=PATH_HEADER+"/{id}", produces={"application/json"})
    public ResponseEntity<ToDoDTO> getToDo(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long id
    ){
        return new ResponseEntity<>(
                toDoService.getToDo(user, id),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value=PATH_HEADER)
    public ResponseEntity<?> deleteToDos(
        @AuthenticationPrincipal User user
    ){
        toDoService.deleteUserToDos(user);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(value=PATH_HEADER+"/{id}")
    public ResponseEntity<?> deleteToDo(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long id
    ){
        toDoService.deleteToDo(user, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value=PATH_HEADER, produces={"application/json"}, consumes={"application/json"})
    public ResponseEntity<ToDoDTO> updateToDo(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ToDoUpdateDTO toDoUpdateDTO
            ){
        return new ResponseEntity(
                toDoService.updateToDo(user, toDoUpdateDTO),
                HttpStatus.OK
        );
    }

}

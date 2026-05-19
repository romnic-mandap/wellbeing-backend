package com.example.meal2.todo;

import com.example.meal2.constant.Constants;
import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceLimitException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.todo.dto.ToDoCreationDTO;
import com.example.meal2.todo.dto.ToDoDTO;
import com.example.meal2.todo.dto.ToDoUpdateDTO;
import com.example.meal2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    @Autowired
    public ToDoServiceImpl(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public ToDoDTO addToDo(User user, ToDoCreationDTO toDoCreationDTO) {
        Integer count = countUserToDos(user);
        if(count >= Constants.DEFAULT_TO_DO_SIZE){
            throw new ResourceLimitException("ToDo exceeds " + Constants.DEFAULT_TO_DO_SIZE);
        }
        ToDo todo = new ToDo();
        todo.setUserId(user.getId());
        todo.setDescription(toDoCreationDTO.description());
        todo.setIsDone(false);
        return convertToDoToToDoDTO(toDoRepository.save(todo));
    }

    @Override
    public ToDoDTO getToDo(User user, Long toDoId) {
        ToDo toDo = toDoRepository.findById(toDoId).orElseThrow(
                () -> new ResourceNotFoundException("ToDo not found with id: " + toDoId)
        );
        if(!Objects.equals(user.getId(), toDo.getUserId())){
            throw new NotResourceOwnerException("does not own this resource");
        }
        return convertToDoToToDoDTO(toDo);
    }

    @Override
    public List<ToDoDTO> getUserToDos(User user) {
        return toDoRepository.getAllUserToDos(user.getId()).stream()
                .map(this::convertToDoToToDoDTO).toList();
    }

    @Override
    public void deleteToDo(User user, Long toDoId) {
        ToDo toDo = toDoRepository.findById(toDoId).orElseThrow(
                () -> new ResourceNotFoundException("ToDo not found with id: " + toDoId)
        );
        if(!Objects.equals(user.getId(), toDo.getUserId())){
            throw new NotResourceOwnerException("does not own this resource");
        }
        toDoRepository.deleteById(toDoId);
    }

    @Override
    public void deleteUserToDos(User user) {
        toDoRepository.deleteUserToDos(user.getId());
    }

    @Override
    public ToDoDTO updateToDo(User user, ToDoUpdateDTO toDoUpdateDTO) {
        ToDo toDo = toDoRepository.findById(toDoUpdateDTO.id()).orElseThrow(
                () -> new ResourceNotFoundException("ToDo not found with id: " + toDoUpdateDTO.id())
        );
        if(!Objects.equals(user.getId(), toDo.getUserId())){
            throw new NotResourceOwnerException("does not own this resource");
        }
        toDo.setIsDone(toDoUpdateDTO.isDone());
        return convertToDoToToDoDTO(toDoRepository.save(toDo));
    }

    @Override
    public Integer countUserToDos(User user) {
        return toDoRepository.countUserToDos(user.getId());
    }

    private ToDoDTO convertToDoToToDoDTO(ToDo toDo){
        return new ToDoDTO(
                toDo.getId(),
                toDo.getUserId(),
                toDo.getDescription(),
                toDo.getIsDone()
        );
    }
}

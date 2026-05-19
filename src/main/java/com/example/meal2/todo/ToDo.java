package com.example.meal2.todo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="to_do")
public class ToDo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="user_id")
    private Integer userId;

    @NotBlank(message="description => must not be blank")
    @Size(max=255, message="description => must not exceed 255 characters")
    @Column(name="description", nullable=false)
    private String description;

    @NotNull(message="isDone => must not be blank")
    @Column(name="is_done", nullable=false)
    private Boolean isDone;

}

package com.example.meal2.todo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    @Query(value=
            """
                SELECT COUNT(*)
                FROM to_do td
                WHERE (:uid = td.user_id)
            """, nativeQuery=true)
    Integer countUserToDos(@Param("uid") Integer userId);

    @Query(value=
            """
                SELECT td.*
                FROM to_do td
                WHERE (:uid = td.user_id)
            """, nativeQuery=true)
    List<ToDo> getAllUserToDos(@Param("uid") Integer userId);

    @Modifying
    @Transactional
    @Query(value= """
            DELETE
            FROM to_do td
            WHERE (:uid = td.user_id)
            """, nativeQuery=true)
    void deleteUserToDos(@Param("uid") Integer userId);

}

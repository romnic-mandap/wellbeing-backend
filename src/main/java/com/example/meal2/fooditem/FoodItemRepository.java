package com.example.meal2.fooditem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    @Query(value=
            """
                SELECT COUNT(*)
                FROM food_item fi
            """, nativeQuery=true)
    Integer countFoodItems();

}

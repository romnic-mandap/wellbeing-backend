package com.example.meal2.foodtableitem;

import com.example.meal2.foodtableitem.dto.FoodTableItemDTO;
import com.example.meal2.foodtableitem.dto.FoodTableSummaryDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface FoodTableItemRepository extends JpaRepository<FoodTableItem, Long> {

    @Query(value=
        """
            SELECT COUNT(*)
            FROM food_table_item fti
            WHERE
                (:uid = fti.user_id)
        """, nativeQuery=true)
    Integer countUserFoodTableItems(
            @Param("uid") Integer userId
    );

    @Query(value=
        """
            SELECT 
                SUM(kcal) totalKcal, 
                SUM(carbs) totalCarbs, 
                SUM(fat) totalFat, 
                SUM(protein) totalProtein, 
                SUM(fiber) totalFiber, 
                SUM(sodium) totalSodium, 
                COUNT(1) totalCount  
            FROM food_table_item fti
            WHERE
                (:uid = fti.user_id)  
        """, nativeQuery=true)
    FoodTableSummaryDTO getFoodTableSummary(
            @Param("uid") Integer userId
    );

    List<FoodTableItem> findAllByOrderByCreatedAtAsc();

    @Modifying
    @Transactional
    @Query(value=
            """
                DELETE
                FROM food_table_item fti
                WHERE
                    (:uid = fti.user_id)
            """, nativeQuery=true)
    void deleteFoodTableItems(
            @Param("uid") Integer userId
    );
}

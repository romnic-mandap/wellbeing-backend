package com.example.meal2.foodtableitem;

import com.example.meal2.foodtableitem.dto.FoodTableDTO;
import com.example.meal2.foodtableitem.dto.FoodTableItemCountDTO;
import com.example.meal2.foodtableitem.dto.FoodTableItemDTO;
import com.example.meal2.foodtableitem.dto.FoodTableStatsDTO;
import com.example.meal2.user.User;
import org.springframework.data.domain.Page;

public interface FoodTableItemService {

    FoodTableDTO getFoodTable(User user, Integer page, Integer size);

    FoodTableStatsDTO getFoodTableStats(User user);

    Page<FoodTableItemDTO> getAllFoodTableItemsPage(User user, Integer page, Integer size);

    FoodTableItemDTO createFoodTableItem(User user, Long foodItemId);

    FoodTableItemDTO getFoodTableItem(User user, Long foodTableItemId);

    void deleteFoodTableItem(User user, Long foodTableItemId);

    void deleteFoodTableItems(User user);

    FoodTableItemCountDTO getFoodTableItemCount(User user);

}

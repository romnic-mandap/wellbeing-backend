package com.example.meal2.foodtableitem;

import com.example.meal2.foodtableitem.dto.FoodTableDTO;
import com.example.meal2.foodtableitem.dto.FoodTableItemDTO;
import com.example.meal2.user.User;

public interface FoodTableItemService {

    FoodTableDTO getFoodTable(User user);

    FoodTableItemDTO createFoodTableItem(User user, Long foodItemId);

    void deleteFoodTableItem(User user, Long foodTableItemId);

    void deleteFoodTableItems(User user);

}

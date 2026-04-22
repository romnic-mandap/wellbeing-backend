package com.example.meal2.fooditem;

import com.example.meal2.fooditem.dto.FoodItemDTO;

import java.util.List;
import java.util.Optional;

public interface FoodItemService {

    List<FoodItemDTO> getAllFoodItems();

    FoodItemDTO getFoodItem(Long foodItemId);

}

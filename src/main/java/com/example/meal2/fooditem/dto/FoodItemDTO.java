package com.example.meal2.fooditem.dto;

import com.example.meal2.fooditem.FoodItem;

public record FoodItemDTO(
        Long id,
        String foodName,
        Double servingSize,
        Double kcal,
        Double carbs,
        Double fat,
        Double protein,
        Double fiber,
        Double sodium,
        FoodItem.FoodType foodType,
        String note
) {
}

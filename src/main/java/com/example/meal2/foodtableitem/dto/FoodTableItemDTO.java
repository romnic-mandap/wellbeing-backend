package com.example.meal2.foodtableitem.dto;

import com.example.meal2.fooditem.FoodItem;

import java.time.LocalDateTime;

public record FoodTableItemDTO(
        Long id,
        Integer userId,
        String foodName,
        Double servingSize,
        Double kcal,
        Double carbs,
        Double fat,
        Double protein,
        Double fiber,
        Double sodium,
        FoodItem.FoodType foodType,
        String note,
        LocalDateTime createdAt
) {
}

package com.example.meal2.fooditem.dto;

import com.example.meal2.fooditem.FoodItem;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FoodItemCreationDTO(
        @NotBlank(message="foodName => must not be blank")
        @Size(max=255, message="foodName => must not exceed 255 characters")
        String foodName,
        @NotNull(message="servingSize => must not be blank")
        Double servingSize,
        @NotNull(message="kcal => must not be blank")
        Double kcal,
        @NotNull(message="carbs => must not be blank")
        Double carbs,
        @NotNull(message="fat => must not be blank")
        Double fat,
        @NotNull(message="protein => must not be blank")
        Double protein,
        @NotNull(message="fiber => must not be blank")
        Double fiber,
        @NotNull(message="sodium => must not be blank")
        Double sodium,
        @NotNull(message="foodType => must not be blank (LEGUME, FRUIT, VEGETABLE, GRAIN, MEAT)")
        @Enumerated(EnumType.STRING)
        FoodItem.FoodType foodType,
        @Size(max=255, message="note => must not exceed 255 characters")
        String note
) {
}

package com.example.meal2.foodtableitem.dto;

import java.util.List;

public record FoodTableDTO(
        Double totalKcal,
        Double totalCarbs,
        Double totalFat,
        Double totalProtein,
        Double totalFiber,
        Double totalSodium,
        Integer totalCount,
        List<FoodTableItemDTO> foodTableItems
) {
}

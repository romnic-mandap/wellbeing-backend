package com.example.meal2.foodtableitem.dto;

public record FoodTableStatsDTO(
        Double totalKcal,
        Double totalCarbs,
        Double totalFat,
        Double totalProtein,
        Double totalFiber,
        Double totalSodium
) {
}

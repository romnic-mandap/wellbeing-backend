package com.example.meal2.fooditem;

import com.example.meal2.fooditem.dto.FoodItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemServiceImpl implements FoodItemService {


    private final FoodItemRepository foodItemRepository;

    @Autowired
    public FoodItemServiceImpl(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    @Override
    public List<FoodItemDTO> getAllFoodItems() {
        return foodItemRepository.findAll().stream()
                .map(this::convertFoodItemToFoodItemDTO)
                .toList();
    }

    private FoodItemDTO convertFoodItemToFoodItemDTO(FoodItem foodItem){
        return new FoodItemDTO(
                foodItem.getId(),
                foodItem.getFoodName(),
                foodItem.getServingSize(),
                foodItem.getKcal(),
                foodItem.getCarbs(),
                foodItem.getFat(),
                foodItem.getProtein(),
                foodItem.getFiber(),
                foodItem.getSodium(),
                foodItem.getFoodType(),
                foodItem.getNote()
        );
    }
}

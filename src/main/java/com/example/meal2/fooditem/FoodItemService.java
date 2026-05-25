package com.example.meal2.fooditem;

import com.example.meal2.fooditem.dto.FoodItemCountDTO;
import com.example.meal2.fooditem.dto.FoodItemCreationDTO;
import com.example.meal2.fooditem.dto.FoodItemDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface FoodItemService {

    FoodItemDTO createFoodItem(FoodItemCreationDTO foodItemCreationDTO);

    List<FoodItemDTO> getAllFoodItems(Integer page, Integer size);

    Page<FoodItemDTO> getAllFoodItemsPage(Integer page, Integer size);

    FoodItemDTO getFoodItem(Long foodItemId);

    FoodItemCountDTO getFoodItemCount();

}

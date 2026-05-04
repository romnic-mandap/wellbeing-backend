package com.example.meal2.fooditem;

import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.fooditem.dto.FoodItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FoodItemServiceImpl implements FoodItemService {


    private final FoodItemRepository foodItemRepository;

    @Autowired
    public FoodItemServiceImpl(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    @Override
    public List<FoodItemDTO> getAllFoodItems(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("foodName").ascending()
        );
        return foodItemRepository.findAll(pageable).stream()
                .map(this::convertFoodItemToFoodItemDTO)
                .toList();
    }

    @Override
    public FoodItemDTO getFoodItem(Long foodItemId) {
        Optional<FoodItem> foodItem = foodItemRepository.findById(foodItemId);
        if(foodItem.isPresent()){
            return convertFoodItemToFoodItemDTO(foodItem.get());
        }
        throw new ResourceNotFoundException("foodItem Id not found: " + foodItemId);
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

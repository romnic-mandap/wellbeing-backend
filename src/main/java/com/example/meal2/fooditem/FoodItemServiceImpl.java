package com.example.meal2.fooditem;

import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.fooditem.dto.FoodItemCountDTO;
import com.example.meal2.fooditem.dto.FoodItemCreationDTO;
import com.example.meal2.fooditem.dto.FoodItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
    public FoodItemDTO createFoodItem(FoodItemCreationDTO foodItemCreationDTO) {
        FoodItem foodItem = new FoodItem();
        foodItem.setFoodName(foodItemCreationDTO.foodName());
        foodItem.setServingSize(foodItemCreationDTO.servingSize());
        foodItem.setKcal(foodItemCreationDTO.kcal());
        foodItem.setCarbs(foodItemCreationDTO.carbs());
        foodItem.setFat(foodItemCreationDTO.fat());
        foodItem.setProtein(foodItemCreationDTO.protein());
        foodItem.setFiber(foodItemCreationDTO.fiber());
        foodItem.setSodium(foodItemCreationDTO.sodium());
        foodItem.setFoodType(foodItemCreationDTO.foodType());
        foodItem.setNote(foodItemCreationDTO.note());
        FoodItem savedFoodItem = foodItemRepository.save(foodItem);
        return convertFoodItemToFoodItemDTO(savedFoodItem);
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
    public Page<FoodItemDTO> getAllFoodItemsPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("foodName").ascending()
        );
        Integer count = foodItemRepository.countFoodItems();
        return new PageImpl<>(
                foodItemRepository.findAll(pageable)
                        .stream()
                        .map(this::convertFoodItemToFoodItemDTO)
                        .toList(), pageable, count
        );
    }

    @Override
    public FoodItemDTO getFoodItem(Long foodItemId) {
        Optional<FoodItem> foodItem = foodItemRepository.findById(foodItemId);
        if(foodItem.isPresent()){
            return convertFoodItemToFoodItemDTO(foodItem.get());
        }
        throw new ResourceNotFoundException("foodItem Id not found: " + foodItemId);
    }

    @Override
    public FoodItemCountDTO getFoodItemCount() {
        Integer count = foodItemRepository.countFoodItems();
        return new FoodItemCountDTO(count);
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

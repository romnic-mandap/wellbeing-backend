package com.example.meal2.foodtableitem;

import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceLimitException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.fooditem.FoodItemService;
import com.example.meal2.fooditem.dto.FoodItemDTO;
import com.example.meal2.foodtableitem.dto.FoodTableDTO;
import com.example.meal2.foodtableitem.dto.FoodTableItemDTO;
import com.example.meal2.foodtableitem.dto.FoodTableSummaryDTO;
import com.example.meal2.mealitem.MealItem;
import com.example.meal2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FoodTableItemServiceImpl implements FoodTableItemService {

    private final FoodTableItemRepository foodTableItemRepository;
    private final FoodItemService foodItemService;

    public static final int MAX_FOOD_TABLE_ITEMS = 32;

    @Autowired
    public FoodTableItemServiceImpl(FoodTableItemRepository foodTableItemRepository, FoodItemService foodItemService) {
        this.foodTableItemRepository = foodTableItemRepository;
        this.foodItemService = foodItemService;
    }

    @Override
    public FoodTableDTO getFoodTable(User user) {
        FoodTableSummaryDTO summary = foodTableItemRepository.getFoodTableSummary(user.getId());
        List<FoodTableItemDTO> foodTableItemDTOList = foodTableItemRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(this::convertFoodTableItemToFoodTableItemDTO)
                .toList();
        return new FoodTableDTO(
                summary.getTotalKcal(),
                summary.getTotalCarbs(),
                summary.getTotalFat(),
                summary.getTotalProtein(),
                summary.getTotalFiber(),
                summary.getTotalSodium(),
                summary.getTotalCount(),
                foodTableItemDTOList
        );
    }

    @Override
    public FoodTableItemDTO createFoodTableItem(User user, Long foodItemId) {
        int foodTableItems = foodTableItemRepository.countUserFoodTableItems(user.getId());
        if(foodTableItems >= MAX_FOOD_TABLE_ITEMS){
            throw new ResourceLimitException(String.format("max %d foodtableitems reached", MAX_FOOD_TABLE_ITEMS));
        }

        FoodItemDTO foodItemDTO = foodItemService.getFoodItem(foodItemId);

        FoodTableItem foodTableItem = new FoodTableItem();
        foodTableItem.setUserId(user.getId());
        foodTableItem.setFoodName(foodItemDTO.foodName());
        foodTableItem.setServingSize(foodItemDTO.servingSize());
        foodTableItem.setKcal(foodItemDTO.kcal());
        foodTableItem.setCarbs(foodItemDTO.carbs());
        foodTableItem.setFat(foodItemDTO.fat());
        foodTableItem.setProtein(foodItemDTO.protein());
        foodTableItem.setFiber(foodItemDTO.fiber());
        foodTableItem.setSodium(foodItemDTO.sodium());
        foodTableItem.setFoodType(foodItemDTO.foodType());
        foodTableItem.setNote(foodItemDTO.note());
        foodTableItem.setCreatedAt(LocalDateTime.now());

        return convertFoodTableItemToFoodTableItemDTO(foodTableItemRepository.save(foodTableItem));
    }

    @Override
    public void deleteFoodTableItem(User user, Long foodTableItemId) {
        Optional<FoodTableItem> foodTableItem = foodTableItemRepository.findById(foodTableItemId);
        if(foodTableItem.isPresent()){
            if(Objects.equals(foodTableItem.get().getUserId(), user.getId())){
                foodTableItemRepository.deleteById(foodTableItemId);
                return;
            }
            throw new NotResourceOwnerException("does not own this resource");
        }
        throw new ResourceNotFoundException("foodTableItem id not found: " + foodTableItemId);
    }

    @Override
    public void deleteFoodTableItems(User user) {
        foodTableItemRepository.deleteFoodTableItems(user.getId());
    }

    private FoodTableItemDTO convertFoodTableItemToFoodTableItemDTO(FoodTableItem foodTableItem){
        return new FoodTableItemDTO(
                foodTableItem.getId(),
                foodTableItem.getUserId(),
                foodTableItem.getFoodName(),
                foodTableItem.getServingSize(),
                foodTableItem.getKcal(),
                foodTableItem.getCarbs(),
                foodTableItem.getFat(),
                foodTableItem.getProtein(),
                foodTableItem.getFiber(),
                foodTableItem.getSodium(),
                foodTableItem.getFoodType(),
                foodTableItem.getNote(),
                foodTableItem.getCreatedAt()
        );
    }
}

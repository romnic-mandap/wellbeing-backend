package com.example.meal2.foodtableitem;

import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceLimitException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.fooditem.FoodItemService;
import com.example.meal2.fooditem.dto.FoodItemDTO;
import com.example.meal2.foodtableitem.dto.*;
import com.example.meal2.mealitem.MealItem;
import com.example.meal2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.meal2.constant.Constants.DEFAULT_FOOD_TABLE_SIZE;

@Service
public class FoodTableItemServiceImpl implements FoodTableItemService {

    private final FoodTableItemRepository foodTableItemRepository;
    private final FoodItemService foodItemService;

    @Autowired
    public FoodTableItemServiceImpl(FoodTableItemRepository foodTableItemRepository, FoodItemService foodItemService) {
        this.foodTableItemRepository = foodTableItemRepository;
        this.foodItemService = foodItemService;
    }

    @Override
    public FoodTableDTO getFoodTable(User user, Integer page, Integer size) {
        FoodTableSummaryDTO summary = foodTableItemRepository.getFoodTableSummary(user.getId());
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").ascending()
        );
        List<FoodTableItemDTO> foodTableItemDTOList = foodTableItemRepository.getAllFoodTableItems(user.getId(), pageable)
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
    public FoodTableStatsDTO getFoodTableStats(User user) {
        FoodTableSummaryDTO summary = foodTableItemRepository.getFoodTableSummary(user.getId());
        return new FoodTableStatsDTO(
                summary.getTotalKcal(),
                summary.getTotalCarbs(),
                summary.getTotalFat(),
                summary.getTotalProtein(),
                summary.getTotalFiber(),
                summary.getTotalSodium()
        );
    }

    @Override
    public Page<FoodTableItemDTO> getAllFoodTableItemsPage(User user, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("created_at").ascending());
        Integer count = foodTableItemRepository.countUserFoodTableItems(user.getId());
        return new PageImpl<>(foodTableItemRepository.getAllFoodTableItems(user.getId(), pageable)
                .stream()
                .map(this::convertFoodTableItemToFoodTableItemDTO)
                .toList(), pageable, count);
    }

    @Override
    public FoodTableItemDTO createFoodTableItem(User user, Long foodItemId) {
        int foodTableItems = foodTableItemRepository.countUserFoodTableItems(user.getId());
        if(foodTableItems >= DEFAULT_FOOD_TABLE_SIZE){
            throw new ResourceLimitException(String.format("max %d foodtableitems reached", DEFAULT_FOOD_TABLE_SIZE));
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
    public FoodTableItemDTO getFoodTableItem(User user, Long foodTableItemId) {
        Optional<FoodTableItem> foodTableItem = foodTableItemRepository.findById(foodTableItemId);
        if(foodTableItem.isPresent()){
            if(Objects.equals(foodTableItem.get().getUserId(), user.getId())){
                return convertFoodTableItemToFoodTableItemDTO(foodTableItem.get());
            }
            throw new NotResourceOwnerException("does not own this resource");
        }
        throw new ResourceNotFoundException("foodTableItem id not found: " + foodTableItemId);
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

    @Override
    public FoodTableItemCountDTO getFoodTableItemCount(User user) {
        return new FoodTableItemCountDTO(foodTableItemRepository.countUserFoodTableItems(user.getId()));
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

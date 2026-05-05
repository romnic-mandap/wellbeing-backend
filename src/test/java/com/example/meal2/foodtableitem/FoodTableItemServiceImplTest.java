package com.example.meal2.foodtableitem;

import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceLimitException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.fooditem.FoodItem;
import com.example.meal2.fooditem.FoodItemService;
import com.example.meal2.fooditem.FoodItemServiceImpl;
import com.example.meal2.fooditem.dto.FoodItemDTO;
import com.example.meal2.user.Role;
import com.example.meal2.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FoodTableItemServiceImplTest {
    /*
    create
    get
    delete
    delete all
     */

    @Mock
    private FoodTableItemRepository foodTableItemRepository;

    @Mock
    private FoodItemServiceImpl foodItemService;

    @InjectMocks
    private FoodTableItemServiceImpl foodTableItemService;

    private User user;
    private FoodTableItem foodTableItem;
    private FoodItemDTO foodItemDTO;
    private final int USER_ID = 9999;
    private final int OTHER_USER_ID = 9998;
    private final Long FOODTABLEITEM_ID = 10000L;
    private final Long FOODITEM_ID = 1L;
    private final int MAX_FOODTABLEITEMS = 36;

    @BeforeEach
    void setup(){
        user = new User();
        user.setId(USER_ID);
        user.setUsername("test_user");
        user.setPassword("password");
        user.setRole(Role.USER);

        LocalDateTime time = LocalDateTime.now();

        foodTableItem = new FoodTableItem();
        foodTableItem.setId(FOODTABLEITEM_ID);
        foodTableItem.setUserId(USER_ID);
        foodTableItem.setFoodName("Test Food");
        foodTableItem.setServingSize(100.0);
        foodTableItem.setKcal(200.0);
        foodTableItem.setCarbs(50.0);
        foodTableItem.setFat(10.0);
        foodTableItem.setProtein(25.0);
        foodTableItem.setFiber(5.0);
        foodTableItem.setSodium(150.0);
        foodTableItem.setFoodType(FoodItem.FoodType.GRAIN);
        foodTableItem.setCreatedAt(time);

        foodItemDTO = new FoodItemDTO(
                FOODITEM_ID,
                "Test Food",
                100.0,
                200.0,
                50.0,
                10.0,
                25.0,
                5.0,
                150.0,
                FoodItem.FoodType.GRAIN,
                null
        );
    }

    @DisplayName("createFoodTableItem: normal")
    @Test
    void createFoodTableItem_normal(){
        when(foodTableItemRepository.countUserFoodTableItems(user.getId())).thenReturn(1);
        when(foodItemService.getFoodItem(FOODITEM_ID)).thenReturn(foodItemDTO);
        when(foodTableItemRepository.save(any(FoodTableItem.class))).thenReturn(foodTableItem);
        foodTableItemService.createFoodTableItem(user, FOODITEM_ID);
        verify(foodTableItemRepository, times(1)).save(any(FoodTableItem.class));
    }

    @DisplayName("createFoodTableItem: max FoodTableItems")
    @Test
    void createFoodTableItem_normal_maxFoodTableItems(){
        when(foodTableItemRepository.countUserFoodTableItems(user.getId())).thenReturn(MAX_FOODTABLEITEMS);
        ResourceLimitException thrown = Assertions.assertThrows(
                ResourceLimitException.class,
                () -> foodTableItemService.createFoodTableItem(user, FOODITEM_ID)
        );
        Assertions.assertEquals(String.format("max %d foodtableitems reached", MAX_FOODTABLEITEMS), thrown.getMessage());
        verify(foodTableItemRepository, never()).save(any());
    }

    @DisplayName("deleteFoodTableItem: normal")
    @Test
    void deleteFoodTableItem_normal(){
        when(foodTableItemRepository.findById(FOODTABLEITEM_ID)).thenReturn(Optional.of(foodTableItem));
        foodTableItemService.deleteFoodTableItem(user, FOODTABLEITEM_ID);
        verify(foodTableItemRepository, times(1)).deleteById(FOODTABLEITEM_ID);
    }

    @DisplayName("deleteFoodTableItem: does not own resource")
    @Test
    void deleteFoodTableItem_doesNotOwnResource(){
        foodTableItem.setUserId(OTHER_USER_ID);
        when(foodTableItemRepository.findById(FOODTABLEITEM_ID)).thenReturn(Optional.of(foodTableItem));
        NotResourceOwnerException thrown = Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> {foodTableItemService.deleteFoodTableItem(user, FOODTABLEITEM_ID);}
        );
        Assertions.assertEquals("does not own this resource", thrown.getMessage());
        verify(foodTableItemRepository, never()).deleteById(FOODTABLEITEM_ID);
    }

    @DisplayName("deleteFoodTableItem: id not found")
    @Test
    void deleteFoodTableItem_idNotFound(){
        Long nonExistentId = 9999L;
        when(foodTableItemRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        ResourceNotFoundException thrown = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> {foodTableItemService.deleteFoodTableItem(user, nonExistentId);}
        );
        Assertions.assertEquals("foodTableItem id not found: " + nonExistentId, thrown.getMessage());
        verify(foodTableItemRepository, never()).deleteById(nonExistentId);
    }

    @DisplayName("deleteFoodTableItems: normal")
    @Test
    void deleteFoodTableItems_normal(){
        foodTableItemService.deleteFoodTableItems(user);

        verify(foodTableItemRepository, times(1)).deleteFoodTableItems(USER_ID);
    }

}

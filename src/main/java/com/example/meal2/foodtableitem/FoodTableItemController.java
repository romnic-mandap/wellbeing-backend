package com.example.meal2.foodtableitem;

import com.example.meal2.fooditem.dto.FoodItemCountDTO;
import com.example.meal2.fooditem.dto.FoodItemDTO;
import com.example.meal2.foodtableitem.dto.FoodTableDTO;
import com.example.meal2.foodtableitem.dto.FoodTableItemCountDTO;
import com.example.meal2.foodtableitem.dto.FoodTableItemCreationDTO;
import com.example.meal2.foodtableitem.dto.FoodTableItemDTO;
import com.example.meal2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.meal2.constant.Constants.*;

@RestController
@RequestMapping("/api/v1")
public class FoodTableItemController {

    private final FoodTableItemService foodTableItemService;

    @Autowired
    public FoodTableItemController(FoodTableItemService foodTableItemService) {
        this.foodTableItemService = foodTableItemService;
    }

    @PostMapping(value="/food-table-items", produces={"application/json"})
    public ResponseEntity<FoodTableItemDTO> createFoodTableItem(
            @AuthenticationPrincipal User user,
            @RequestBody FoodTableItemCreationDTO foodTableItemCreationDTO
    ){
        return new ResponseEntity<>(
                foodTableItemService.createFoodTableItem(user, foodTableItemCreationDTO.foodItemId()),
                HttpStatus.CREATED
        );
    }

    @GetMapping(value="/food-table-items", produces={"application/json"})
    public ResponseEntity<FoodTableDTO> getAllFoodTableItems(
            @AuthenticationPrincipal User user,
            @RequestParam Optional<Integer> p,
            @RequestParam Optional<Integer> s
    ){
        Integer page = p.orElse(DEFAULT_PAGE_NUMBER);
        Integer size = s.orElse(DEFAULT_FOOD_TABLE_ITEM_PAGE_SIZE);
        return new ResponseEntity<>(
                foodTableItemService.getFoodTable(user, page, size),
                HttpStatus.OK
        );
    }

    @GetMapping(value="/food-table-items/{id}", produces={"application/json"})
    public ResponseEntity<FoodTableItemDTO> getFoodTableItem(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long id
    ){
        return new ResponseEntity<>(
                foodTableItemService.getFoodTableItem(user, id),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value="/food-table-items/{id}")
    public ResponseEntity<?> deleteFoodTableItem(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long id
    ){
        foodTableItemService.deleteFoodTableItem(user, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(value="/food-table-items")
    public ResponseEntity<?> deleteFoodTableItems(
            @AuthenticationPrincipal User user
    ){
        foodTableItemService.deleteFoodTableItems(user);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value="/food-table-items/count", produces={"application/json"})
    public ResponseEntity<FoodTableItemCountDTO> getFoodTableItemCount(
            @AuthenticationPrincipal User user
    ){
        return new ResponseEntity<>(
                foodTableItemService.getFoodTableItemCount(user),
                HttpStatus.OK
        );
    }
}

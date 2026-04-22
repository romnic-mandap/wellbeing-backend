package com.example.meal2.foodtableitem;

import com.example.meal2.foodtableitem.dto.FoodTableDTO;
import com.example.meal2.foodtableitem.dto.FoodTableItemDTO;
import com.example.meal2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody Long foodItemId
    ){
        return new ResponseEntity<>(
                foodTableItemService.createFoodTableItem(user, foodItemId),
                HttpStatus.CREATED
        );
    }

    @GetMapping(value="/food-table-items", produces={"application/json"})
    public ResponseEntity<FoodTableDTO> getAllFoodTableItems(
            @AuthenticationPrincipal User user
    ){
        return new ResponseEntity<>(
                foodTableItemService.getFoodTable(user),
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
}

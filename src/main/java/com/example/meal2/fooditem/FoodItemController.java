package com.example.meal2.fooditem;

import com.example.meal2.fooditem.dto.FoodItemCountDTO;
import com.example.meal2.fooditem.dto.FoodItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.meal2.constant.Constants.DEFAULT_FOOD_ITEM_PAGE_SIZE;
import static com.example.meal2.constant.Constants.DEFAULT_PAGE_NUMBER;

@RestController
@RequestMapping("/api/v1")
public class FoodItemController {

    private final FoodItemService foodItemService;

    @Autowired
    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping(value="/food-items", produces={"application/json"})
    public ResponseEntity<List<FoodItemDTO>> getAllFoodItems(
            @RequestParam Optional<Integer> p,
            @RequestParam Optional<Integer> s
    ){
        Integer page = p.orElse(DEFAULT_PAGE_NUMBER);
        Integer size = s.orElse(DEFAULT_FOOD_ITEM_PAGE_SIZE);
        return new ResponseEntity<>(
                foodItemService.getAllFoodItems(page, size),
                HttpStatus.OK
        );
    }

    @GetMapping(value="/food-items/{id}", produces={"application/json"})
    public ResponseEntity<FoodItemDTO> getFoodItem(
            @PathVariable("id") Long id
    ){
        return new ResponseEntity<>(
                foodItemService.getFoodItem(id),
                HttpStatus.OK
        );
    }

    @GetMapping(value="/food-items/count", produces={"application/json"})
    public ResponseEntity<FoodItemCountDTO> getFoodItemCount(){
        return new ResponseEntity<>(
                foodItemService.getFoodItemCount(),
                HttpStatus.OK
        );
    }
}

package com.example.meal2.fooditem;

import com.example.meal2.fooditem.dto.FoodItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FoodItemController {

    private final FoodItemService foodItemService;

    @Autowired
    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping(value="/food-items", produces={"application/json"})
    public ResponseEntity<List<FoodItemDTO>> getAllFoodItems(){
        return new ResponseEntity<>(
                foodItemService.getAllFoodItems(),
                HttpStatus.OK
        );
    }
}

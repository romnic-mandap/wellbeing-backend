package com.example.meal2.fooditem;

import com.example.meal2.fooditem.dto.FoodItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.example.meal2.constant.Constants.DEFAULT_FOOD_ITEM_PAGE_SIZE;
import static com.example.meal2.constant.Constants.DEFAULT_PAGE_NUMBER;

@RestController
@RequestMapping("/api/v2")
public class FoodItemControllerV2 {

    private final FoodItemService foodItemService;
    @Autowired
    public FoodItemControllerV2(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping(value="/food-items", produces={"application/json"})
    public ResponseEntity<Page<FoodItemDTO>> getAllFoodItems(
            @RequestParam Optional<Integer> p,
            @RequestParam Optional<Integer> s
    ){
        Integer page = p.orElse(DEFAULT_PAGE_NUMBER);
        Integer size = s.orElse(DEFAULT_FOOD_ITEM_PAGE_SIZE);
        return new ResponseEntity<>(
                foodItemService.getAllFoodItemsPage(page, size),
                HttpStatus.OK
        );
    }
}

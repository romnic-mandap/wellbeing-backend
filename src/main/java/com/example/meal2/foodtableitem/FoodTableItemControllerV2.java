package com.example.meal2.foodtableitem;

import com.example.meal2.foodtableitem.dto.FoodTableDTO;
import com.example.meal2.foodtableitem.dto.FoodTableItemDTO;
import com.example.meal2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.example.meal2.constant.Constants.DEFAULT_FOOD_TABLE_ITEM_PAGE_SIZE;
import static com.example.meal2.constant.Constants.DEFAULT_PAGE_NUMBER;

@RestController
@RequestMapping("/api/v2")
public class FoodTableItemControllerV2 {

    private final FoodTableItemService foodTableItemService;

    @Autowired
    public FoodTableItemControllerV2(FoodTableItemService foodTableItemService) {
        this.foodTableItemService = foodTableItemService;
    }

    @GetMapping(value="/food-table-items", produces={"application/json"})
    public ResponseEntity<Page<FoodTableItemDTO>> getAllFoodTableItems(
            @AuthenticationPrincipal User user,
            @RequestParam Optional<Integer> p,
            @RequestParam Optional<Integer> s
    ){
        Integer page = p.orElse(DEFAULT_PAGE_NUMBER);
        Integer size = s.orElse(DEFAULT_FOOD_TABLE_ITEM_PAGE_SIZE);
        return new ResponseEntity<>(
                foodTableItemService.getAllFoodTableItemsPage(user, page, size),
                HttpStatus.OK
        );
    }
}

package com.example.meal2.foodtableitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class FoodTableItemControllerV2 {

    private final FoodTableItemService foodTableItemService;

    @Autowired
    public FoodTableItemControllerV2(FoodTableItemService foodTableItemService) {
        this.foodTableItemService = foodTableItemService;
    }


}

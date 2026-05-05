package com.example.meal2.foodtableitem;

import com.example.meal2.fooditem.FoodItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="food_table_item")
public class FoodTableItem {
    public enum FoodType {
        LEGUME, FRUIT, VEGETABLE, GRAIN, MEAT
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="food_name")
    private String foodName;

    @Column(name="serving_size_g")
    private Double servingSize;

    @Column(name="kcal")
    private Double kcal;

    @Column(name="carbs")
    private Double carbs;

    @Column(name="fat")
    private Double fat;

    @Column(name="protein")
    private Double protein;

    @Column(name="fiber")
    private Double fiber;

    @Column(name="sodium")
    private Double sodium;

    @Enumerated(EnumType.STRING)
    @Column(name="food_type", columnDefinition="ENUM('LEGUME','FRUIT','VEGETABLE','GRAIN','MEAT')", nullable=false)
    private FoodItem.FoodType foodType;

    @Column(name="note", nullable=true)
    private String note;

    @Column(name="created_at")
    private LocalDateTime createdAt;
}

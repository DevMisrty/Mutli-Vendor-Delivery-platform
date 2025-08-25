package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RestaurantRequestDto {

    private String restaurantName;
    private String ownerName;
    private String email;
    private String password;
    private List<Cuisine> menu;
    private Integer review;
}

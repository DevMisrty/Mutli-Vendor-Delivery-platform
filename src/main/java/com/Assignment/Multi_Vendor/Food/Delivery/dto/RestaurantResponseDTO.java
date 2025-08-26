package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RestaurantResponseDTO {

    private Long id;
    private String restaurantName;
    private String ownerName;
    private String email;
    private Float ratings;
    private List<Dishes> menu;
}

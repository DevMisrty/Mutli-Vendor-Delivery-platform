package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AdminRestaurantResponseDto {

    private String email;
    private String restaurantName;
    private String ownerName;
    private STATUS status;
}

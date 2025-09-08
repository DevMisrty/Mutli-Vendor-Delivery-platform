package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import lombok.*;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class RestaurantOwnerDto {

    private String restaurantName;
    private String ownerName;
    private String email;

}

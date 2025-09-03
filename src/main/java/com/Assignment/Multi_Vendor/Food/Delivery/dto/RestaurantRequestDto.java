package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RestaurantRequestDto {

    @NotNull(message = "Restaurant name cannot be null")
    @NotBlank(message = "Restaurant name cannot be empty")
    @Min(3)
    private String restaurantName;

    @NotNull(message = "Owner name cannot be null")
    @Min(3)
    private String ownerName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Min(4) @Max(12)
    private String password;
    private List<Cuisine> menu;
    private Float review;
}

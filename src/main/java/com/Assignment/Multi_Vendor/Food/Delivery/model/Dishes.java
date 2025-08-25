package com.Assignment.Multi_Vendor.Food.Delivery.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Embeddable
public class Dishes {
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;
    private double price;
}

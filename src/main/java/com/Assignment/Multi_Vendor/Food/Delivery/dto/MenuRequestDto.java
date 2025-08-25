package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MenuRequestDto {
    private List<Dishes> menu;
}

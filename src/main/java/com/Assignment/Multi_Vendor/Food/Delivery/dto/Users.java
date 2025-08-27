package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import com.Assignment.Multi_Vendor.Food.Delivery.model.ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    private String email;
    private String role;
}

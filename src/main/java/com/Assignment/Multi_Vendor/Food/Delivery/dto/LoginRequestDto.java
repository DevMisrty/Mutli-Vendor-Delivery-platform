package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class LoginRequestDto {

    private String email    ;
    private String password;
}

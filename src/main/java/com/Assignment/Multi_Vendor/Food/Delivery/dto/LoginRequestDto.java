package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class LoginRequestDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}

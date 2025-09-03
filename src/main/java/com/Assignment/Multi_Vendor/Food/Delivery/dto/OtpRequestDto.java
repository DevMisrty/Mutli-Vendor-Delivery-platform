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
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OtpRequestDto {

    @Email @NotNull
    private String email;

    @NotNull
    @Size(min = 4, max = 6)
    private Integer otp;

}

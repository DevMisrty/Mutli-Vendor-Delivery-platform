package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CustomerDto {


    private long id;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Min(4)
    private String password;

    @NotNull @Min(3)
    private String firstName;
    private String lastName;

    @NotNull @Min(5) @Max(12)
    private String phoneNumber;

    @NotNull @Min(5)
    private String address; 
}

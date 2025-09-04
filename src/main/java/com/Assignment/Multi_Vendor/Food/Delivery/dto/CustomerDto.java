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
    @Size(min = 3, message = "Password must be at least 3 characters long")
    private String password;

    @NotNull @Size(min = 3, message = " FirstName must be at least 5 characters long")
    private String firstName;
    private String lastName;

    @NotNull
    private String phoneNumber;

    @NotNull @Size(min = 5, message = "Address must be at least 5 characters long")
    private String address; 
}

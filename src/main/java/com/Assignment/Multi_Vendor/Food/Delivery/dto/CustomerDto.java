package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CustomerDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address; 
}

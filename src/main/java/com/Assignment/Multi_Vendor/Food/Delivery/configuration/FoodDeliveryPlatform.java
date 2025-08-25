package com.Assignment.Multi_Vendor.Food.Delivery.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FoodDeliveryPlatform {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}

package com.Assignment.Multi_Vendor.Food.Delivery.converter;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DishesRepository;
import org.springframework.stereotype.Service;

@Service
public class DishToDishResponseDtoConverter {

    public DishesResponseDto convertDishToDishResponseDto(Dishes dishes){
        return new DishesResponseDto(
                dishes.getRestaurant().getRestaurantName(),
                dishes.getName(),
                dishes.getDescription(),
                dishes.getCuisine(),
                dishes.getPrice(),
                dishes.getRating()
        );
    }
}

package com.Assignment.Multi_Vendor.Food.Delivery.service;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import java.util.List;

public interface DishesService {

    List<DishesResponseDto> getMenuBasedOnCuisine(Cuisine selectedCuisine);

    List<DishesResponseDto> getMenuBasedOnStar(Integer star);
}

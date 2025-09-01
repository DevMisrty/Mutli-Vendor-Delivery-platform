package com.Assignment.Multi_Vendor.Food.Delivery.service;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectInputException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.NoSuchCuisineFound;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import java.util.List;

public interface DishesService {

    List<DishesResponseDto> getMenuBasedOnCuisine(Cuisine selectedCuisine) throws NoSuchCuisineFound;

    List<DishesResponseDto> getMenuBasedOnStar(Integer star) throws IncorrectInputException;

    Dishes addRatingsToDish(String restName, String dishName, Float ratings);
}

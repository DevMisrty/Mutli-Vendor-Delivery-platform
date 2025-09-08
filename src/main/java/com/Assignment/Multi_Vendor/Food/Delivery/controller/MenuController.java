package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectInputException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.NoSuchCuisineFound;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DishesService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.ApiResponseGenerator;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final RestaurantService restaurantService;
    private final DishesService dishesService;
    private final ModelMapper modelMapper;

    @GetMapping("/rest/{restsName}")
    public ResponseEntity<ApiResponse<List<Dishes>>> getMenuOfRestaurant(@PathVariable String restsName)
            throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantService.checkIfRestaurantExistsAndApproved(restsName);
            if(restaurant==null){
                throw new RestaurantNotFoundException("No such Restaurant found");
            }
        List<Dishes> menu = restaurantService.getMenuByRestaurantName(restsName);
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.OK,
                        MessageConstants.MENU_FETCH_SUCCESS,
                        menu
                );
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<ApiResponse<List<DishesResponseDto>>> getMenuBasedOnCuisine
            (@PathVariable String cuisine )
            throws NoSuchCuisineFound {

        Cuisine selectedCuisine = Cuisine.valueOf(cuisine.toUpperCase());
        List<DishesResponseDto> dishes = dishesService.getMenuBasedOnCuisine(selectedCuisine);

        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.OK,
                        MessageConstants.MENU_FETCH_SUCCESS,
                        dishes
                );
    }

    @GetMapping("/rating/{star}")
    public ResponseEntity<ApiResponse<List<DishesResponseDto>>> getMenuBasedOnRating
            (@PathVariable Float star)
            throws IncorrectInputException {
        List<DishesResponseDto> menu = dishesService.getMenuBasedOnStar(star);
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.OK,
                        MessageConstants.MENU_FETCH_SUCCESS,
                        menu
                );
    }

}

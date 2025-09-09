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

/**
 * REST controller for handling menu-related operations.
 * Provides endpoints for retrieving menu items based on different criteria
 * such as restaurant, cuisine type, and rating.
 * */
 @RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final RestaurantService restaurantService;
    private final DishesService dishesService;
    private final ModelMapper modelMapper;

    /**
     * Retrieves the menu for a specific restaurant.
     *
     * @param restsName The name of the restaurant whose menu is to be retrieved
     * @return ResponseEntity containing the list of dishes in the restaurant's menu
     * @throws RestaurantNotFoundException if the specified restaurant is not found
     *
     * @apiNote This endpoint returns all dishes available at the specified restaurant.
     *          The restaurant must be approved to appear in the results.
     *
     * @example GET /menu/rest/Dominos
     */
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

    /**
     * Retrieves menu items filtered by cuisine type.
     *
     * @param cuisine The type of cuisine to filter by (e.g., ITALIAN, CHINESE)
     * @return ResponseEntity containing the list of dishes matching the cuisine
     * @throws NoSuchCuisineFound if the specified cuisine type is not recognized
     *
     * @apiNote This endpoint is case-insensitive for cuisine names.
     *          It converts the input to uppercase to match the enum values.
     *
     * @example GET /menu/cuisine/italian
     */
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

    /**
     * Retrieves menu items with a rating greater than or equal to the specified value.
     *
     * @param star The minimum rating value (1.0 to 5.0)
     * @return ResponseEntity containing the list of dishes meeting the rating criteria
     * @throws IncorrectInputException if the rating is outside the valid range
     *
     * @apiNote The rating scale is from 1.0 to 5.0. The endpoint will return
     *          all dishes with a rating greater than or equal to the specified value.
     *
     * @example GET /menu/rating/4.2 , GET /menu/rating/4
     */
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

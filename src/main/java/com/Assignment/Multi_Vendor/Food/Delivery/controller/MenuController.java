package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DishesService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final RestaurantService restaurantService;
    private final DishesService dishesService;
    private final ModelMapper modelMapper;

    @GetMapping("/resta/{restsName}")
    public ResponseEntity<ApiResponse<List<Dishes>>> getMenuOfRestaurant(@PathVariable String restsName){
        List<Dishes> menu = restaurantService.getMenuByResturantName(restsName);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Menu of " + restsName,
                        menu
                ));
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<ApiResponse<List<DishesResponseDto>>> getMenuBasedOnCuisine(@PathVariable String cuisine ){
        Cuisine selectedCuisine = Cuisine.valueOf(cuisine.toUpperCase());
        List<DishesResponseDto> dishes = dishesService.getMenuBasedOnCuisine(selectedCuisine);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Menu based on " + selectedCuisine,
                        dishes
                ));
    }

    @GetMapping("/rating/{star}")
    public ResponseEntity<ApiResponse<List<DishesResponseDto>>> getMenuBasedOnRating(@PathVariable Integer star){
        List<DishesResponseDto> menu = dishesService.getMenuBasedOnStar(star);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Menu based on Rating " + star +" or greater ",
                        menu
                ));
    }

}

package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.converter.PojoToDtoConverter;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.RestaurantResponseDTO;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DishesService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedBackApiController {

    private final RestaurantService restaurantService;
    private final DishesService dishesService;
    private final ModelMapper modelMapper;
    private final PojoToDtoConverter converter;

    // adds the review to the dish
    @GetMapping("/dish/{restName}/{dishName}/{ratings}")
    public ResponseEntity<ApiResponse<DishesResponseDto>> giveFeedbackToDish(
            @PathVariable String restName,
            @PathVariable String dishName,
            @PathVariable Float ratings
    ){
        if(ratings>5)ratings = 5f;
        Dishes dish = dishesService.addRatingsToDish(restName, dishName, ratings);

        String message = "Ur feedback has been added, Restaurant " + restName +
                ", DishName "+ dishName +
                ", Ratings " + dish.getRating();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        message,
                        converter.convertDishToDishResponseDto(dish)
                ));
    }


    // adds review to the restaurants
    @GetMapping("/rest/{restName}/{ratings}")
    public ResponseEntity<ApiResponse<?>> addRatingsToRest(
            @PathVariable String restName,
            @PathVariable Float ratings
    ){
        if(ratings >5) ratings = 5f;
        Restaurant restaurant = restaurantService.addRatingToRest(restName, ratings);

        String message = " Your feedback has been added, Restaurant " +  restaurant.getRestaurantName()+
                ", with ratings " + restaurant.getRatings();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        message,
                        modelMapper.map(restaurant, RestaurantResponseDTO.class)
                ));
    }
}

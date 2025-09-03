package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.AdminRestaurantResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;

    // fetches the List of Restaurants that are waiting for the admin to approve, status -> NOT_APPROVED
    @GetMapping("/toApprove")
    public ResponseEntity<ApiResponse<List<AdminRestaurantResponseDto>>> getListOfRestaurantToApprove() {
        List<AdminRestaurantResponseDto> restaurants =
                restaurantService.getAllNotApprovedRestaurant()
                        .stream()
                        .map((restaurant ->
                                modelMapper.map(restaurant, AdminRestaurantResponseDto.class)))
                        .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        " list of All restaurants to be approve",
                        restaurants
                ));
    }

    // approves the restaurants, by changing the status, NOT_APPROVED -> APPROVED.
    @GetMapping("/approved/{restId}")
    public ResponseEntity<ApiResponse<AdminRestaurantResponseDto>> approveTheRestaurant
            (@PathVariable Long restId)
            throws RestaurantNotFoundException {

        Restaurant restaurant = restaurantService.approvedRestaurant(restId);
        AdminRestaurantResponseDto response = modelMapper.map(restaurant, AdminRestaurantResponseDto.class);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>(
                        HttpStatus.ACCEPTED.value(),
                        restId + " has been approved. ",
                        response
                ));

    }

    @GetMapping("/disapproved/{restId}")
    public ResponseEntity<ApiResponse<AdminRestaurantResponseDto>> disapproveTheRestaurant
            (@PathVariable Long restId)
            throws RestaurantNotFoundException {

        Restaurant restaurant = restaurantService.disApproveTheRestaurant(restId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>(
                        HttpStatus.ACCEPTED.value(),
                        restId + " has been disapproved",
                        modelMapper.map(restaurant, AdminRestaurantResponseDto.class)
                ));
    }

    // Fetches all the restaurants, irrelevant to whether it is approved by admin, or not.
    @GetMapping("/allRestaurants")
    public ResponseEntity<ApiResponse<List<AdminRestaurantResponseDto>>> getAllRestaurant() {
        List<AdminRestaurantResponseDto> allRestaurants = restaurantService.getAllRestaurant()
                .stream()
                .map(restaurant -> modelMapper.map(restaurant, AdminRestaurantResponseDto.class))
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "List of all restaurants, approved as well as disapproved. ",
                        allRestaurants
                ));
    }
}

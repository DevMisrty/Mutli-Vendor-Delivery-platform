package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.AdminRestaurantResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.RestaurantResponseDTO;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;

    // fetches the List of Restaurants that are waiting for the admin to approve, status -> NOT_APPROVED
    @GetMapping("/toApprove")
    public ResponseEntity<List<AdminRestaurantResponseDto>> getListOfRestaurantToApprove(){
       List<AdminRestaurantResponseDto> restaurants =
               restaurantService.getAllNotApprovedRestaurant()
               .stream()
               .map((restaurant ->
                       modelMapper.map(restaurant, AdminRestaurantResponseDto.class)))
               .toList();
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(restaurants);
    }

    // approves the restaurants, by changing the status, NOT_APPROVED -> APPROVED.
    @GetMapping("/approved/{restId}")
    public ResponseEntity<AdminRestaurantResponseDto> approveTheRestaurant(@PathVariable Long restId){
        Restaurant restaurant = restaurantService.approvedRestaurant(restId);
        AdminRestaurantResponseDto response = modelMapper.map(restaurant, AdminRestaurantResponseDto.class);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @GetMapping("/disapproved/{restId}")
    public ResponseEntity<?> disapproveTheRestaurant(@PathVariable Long restId){
        Restaurant restaurant = restaurantService.disApproveTheRestaurant(restId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(modelMapper.map(restaurant, AdminRestaurantResponseDto.class));
    }

    // Fetches all the restaurants, irrelevant to whether it is approved by admin, or not.
    @GetMapping("/allRestaurants")
    public ResponseEntity<List<AdminRestaurantResponseDto>> getAllRestaurant(){
        List<AdminRestaurantResponseDto> allRestaurants = restaurantService.getAllRestaurant()
                .stream()
                .map(restaurant -> modelMapper.map(restaurant,AdminRestaurantResponseDto.class))
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allRestaurants);
    }
}

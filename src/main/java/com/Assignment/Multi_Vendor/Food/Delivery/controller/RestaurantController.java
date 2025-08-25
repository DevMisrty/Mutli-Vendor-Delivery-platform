package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.MenuRequestDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.RestaurantRequestDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.RestaurantResponseDTO;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/resta")
@RequiredArgsConstructor
public class RestaurantController {


    private final ModelMapper modelMapper;
    private final RestaurantService restaurantService;

    // Adds the new restaurant to Be approved by admin
    @PostMapping("/addRestaurant")
    public ResponseEntity<RestaurantResponseDTO> addNewRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto){
        Restaurant restaurant = modelMapper.map(restaurantRequestDto, Restaurant.class);
        log.info("restaurant : {}",restaurant);
        restaurant = restaurantService.addNewRestaurant(restaurant);
        RestaurantResponseDTO responseDto = modelMapper.map(restaurant, RestaurantResponseDTO.class);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // Adds the new Menu, replacing the old menu, if present.
    @PostMapping("/addMenu/{restId}")
    public ResponseEntity<RestaurantResponseDTO> addNewMenu(@RequestBody MenuRequestDto menurequestDto, @PathVariable Long restId){
        log.info("List<Dishes> : {}", menurequestDto.getMenu());
        Restaurant restaurant = restaurantService.addNewMenu(menurequestDto.getMenu(), restId);
        RestaurantResponseDTO response = modelMapper.map(restaurant, RestaurantResponseDTO.class);
        log.info("Restaurant Info :{}",response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Adds the new Dish in the existing menu.
    @PostMapping("/addDishes/{restId}")
    public ResponseEntity<RestaurantResponseDTO> addNewDishesInMenu(@RequestBody List<Dishes> dishes, @PathVariable Long restId){
        Restaurant restaurant = restaurantService.addDishesToMenu(dishes, restId);
        RestaurantResponseDTO responseDto = modelMapper.map(restaurant, RestaurantResponseDTO.class);
        log.info("Restaurant : {}", responseDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // Deletes the Dish from the Menu.
    @DeleteMapping("/deleteDish/{restId}/{dishName}")
    public ResponseEntity<?> deleteDishFromMenu(@PathVariable Long restId, @PathVariable String dishName){
        Restaurant restaurant = restaurantService.removeDishFromMenu(restId, dishName);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body( modelMapper.map(restaurant,RestaurantResponseDTO.class) );
    }
}

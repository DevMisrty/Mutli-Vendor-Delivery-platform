package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.DishNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectInputException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.MenuRequestDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.RestaurantRequestDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.RestaurantResponseDTO;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.ApiResponseGenerator;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.MessageConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class RestaurantController {


    private final ModelMapper modelMapper;
    private final RestaurantService restaurantService;


    // Adds the new Menu, replacing the old menu, if present.
    @PostMapping("/addMenu/{restId}")
    public ResponseEntity<ApiResponse<RestaurantResponseDTO>> addNewMenu
            (@Valid @RequestBody MenuRequestDto menurequestDto,
             @PathVariable Long restId) throws RestaurantNotFoundException {

        log.info("List<Dishes> : {}", menurequestDto.getMenu());
        Restaurant restaurant = restaurantService.addNewMenu(menurequestDto.getMenu(), restId);
        RestaurantResponseDTO response = modelMapper.map(restaurant, RestaurantResponseDTO.class);
        log.info("Restaurant Info :{}",response);
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.CREATED,
                        MessageConstants.MENU_ADDED,
                        response
                );
    }

    // Adds the new Dish in the existing menu.
     @PostMapping("/addDishes/{restId}")
    public ResponseEntity<ApiResponse<RestaurantResponseDTO>> addNewDishesInMenu(
            @Valid @RequestBody List<Dishes> dishes,
            @PathVariable Long restId) throws IncorrectInputException, RestaurantNotFoundException {

        Restaurant restaurant = restaurantService.addDishesToMenu(dishes, restId);
        RestaurantResponseDTO responseDto = modelMapper.map(restaurant, RestaurantResponseDTO.class);
        log.info("Restaurant : {}", responseDto);
         return ApiResponseGenerator
                 .generateSuccessfulApiResponse(
                         HttpStatus.CREATED,
                         MessageConstants.DISH_ADDED,
                         responseDto
                 );
    }

    // Deletes the Dish from the Menu.
    @DeleteMapping("/deleteDish/{restName}/{dishName}")
    public ResponseEntity<ApiResponse<RestaurantResponseDTO>> deleteDishFromMenu(
            @PathVariable String restName,
            @PathVariable String dishName) throws RestaurantNotFoundException, DishNotFoundException {
        Restaurant restaurant = restaurantService.removeDishFromMenu(restName, dishName);
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.ACCEPTED,
                        MessageConstants.DISH_DELETED,
                        modelMapper.map(restaurant,RestaurantResponseDTO.class)
                );
    }
}

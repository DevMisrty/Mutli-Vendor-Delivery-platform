package com.Assignment.Multi_Vendor.Food.Delivery.service;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.DishNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectInputException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    List<Restaurant> getAllNotApprovedRestaurant();

    Restaurant approvedRestaurant(Long restId) throws RestaurantNotFoundException;

    Restaurant addNewRestaurant(Restaurant restaurant) throws RestaurantNameAlreadyTakenException;

    Restaurant addNewMenu(List<Dishes> menu, Long restId) throws RestaurantNotFoundException;

    Restaurant addDishesToMenu(List<Dishes> dishes, Long restId) throws RestaurantNotFoundException, IncorrectInputException;

    List<Restaurant> getAllRestaurant();

    Restaurant removeDishFromMenu(String restName, String dishName) throws DishNotFoundException, RestaurantNotFoundException;

    Restaurant disApproveTheRestaurant(Long restId) throws RestaurantNotFoundException;

    List<Dishes> getMenuByRestaurantName(String restsName);

    Restaurant addRatingToRest(String restName, Float ratings) throws RestaurantNotFoundException;

    Restaurant getRestaurantByEmail(String email);

    Optional<Restaurant> getRestaurantByName(String restsName);

    Restaurant checkIfRestaurantExists(String email) throws RestaurantNotFoundException;

    Restaurant checkIfRestaurantExistsAndApproved(String restsName);
}

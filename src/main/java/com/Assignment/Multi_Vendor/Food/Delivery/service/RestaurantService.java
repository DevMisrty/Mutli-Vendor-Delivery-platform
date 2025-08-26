package com.Assignment.Multi_Vendor.Food.Delivery.service;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> getAllNotApprovedRestaurant();

    Restaurant approvedRestaurant(Long restId);

    Restaurant addNewRestaurant(Restaurant restaurant);

    Restaurant addNewMenu(List<Dishes> menu, Long restId);

    Restaurant addDishesToMenu(List<Dishes> dishes, Long restId);

    List<Restaurant> getAllRestaurant();

    Restaurant removeDishFromMenu(String restName, String dishName);

    Restaurant disApproveTheRestaurant(Long restId);

    List<Dishes> getMenuByRestaurantName(String restsName);

    Restaurant addRatingToRest(String restName, Float ratings);
}

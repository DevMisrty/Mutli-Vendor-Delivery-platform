package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.model.STATUS;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.RestaurantRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImplementation implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> getAllNotApprovedRestaurant() {
        return restaurantRepository.findAllByStatus(STATUS.NOT_APPROVED);
    }

    @Override
    public Restaurant approvedRestaurant(Long restId) {
        Restaurant restaurant = restaurantRepository.findById(restId).orElseThrow();
        restaurant.setStatus(STATUS.APPROVED);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant addNewRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant addNewMenu(List<Dishes> menu, Long restId) {
        Restaurant restaurant = restaurantRepository.findById(restId).orElseThrow();
        restaurant.setMenu(menu);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant addDishesToMenu(List<Dishes> dishes, Long restId) {
        Restaurant restaurant = restaurantRepository.findById(restId).orElseThrow();
        restaurant.getMenu().addAll(dishes);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant removeDishFromMenu(Long restId, String dishName) {
        Restaurant restaurant = restaurantRepository.findById(restId).orElseThrow();
        List<Dishes> dishes = restaurant.getMenu()
                .stream()
                .filter(dish -> !dish.getName().equals(dishName))
                .collect(Collectors.toList());
        restaurant.setMenu(dishes);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant disApproveTheRestaurant(Long restId) {
        Restaurant restaurant = restaurantRepository.findById(restId).orElseThrow();
        if(restaurant.getStatus()!= STATUS.NOT_APPROVED) {
            return restaurant;
        }
        restaurant.setStatus(STATUS.NOT_APPROVED);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public List<Dishes> getMenuByResturantName(String restsName) {
        return restaurantRepository.findByRestaurantName(restsName).getMenu();
    }
}

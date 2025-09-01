package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.model.STATUS;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DishesRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.RestaurantRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImplementation implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final DishesRepository dishesRepository;

    @Override
    public List<Restaurant> getAllNotApprovedRestaurant() {
        return restaurantRepository.findAllByStatus(STATUS.NOT_APPROVED);
    }

    @Override
    public Restaurant approvedRestaurant(Long restId) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findById(restId)
                .orElseThrow(()-> new RestaurantNotFoundException());
        restaurant.setStatus(STATUS.APPROVED);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant addNewRestaurant(Restaurant restaurant) throws RestaurantNameAlreadyTakenException {
        if(restaurantRepository.existsByRestaurantName(restaurant.getRestaurantName())){
            throw new RestaurantNameAlreadyTakenException();
        }
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant addNewMenu(List<Dishes> menu, Long restId) {
        Restaurant restaurant = restaurantRepository.findById                                                                                      (restId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        List<Dishes> existingMenu = restaurant.getMenu();
        if (existingMenu == null) {
            existingMenu = new ArrayList<>();
            restaurant.setMenu(existingMenu);
        }

        existingMenu.clear();

        for (Dishes dish : menu) {
            dish.setRestaurant(restaurant);
            existingMenu.add(dish);
        }

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant addDishesToMenu(List<Dishes> dishes, Long restId) {
        Restaurant restaurant = restaurantRepository.findById(restId).orElseThrow();
        for (Dishes dish : dishes) {
            dish.setRestaurant(restaurant);
        }
        restaurant.getMenu().addAll(dishes);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant removeDishFromMenu(String restName, String dishName) {
        Dishes dish = dishesRepository.findByNameAndRestaurant_RestaurantName(dishName, restName).orElseThrow();
        Restaurant restaurant = restaurantRepository.findByRestaurantName(restName).orElseThrow();
        restaurant.getMenu().remove(dish);
        dishesRepository.deleteById(dish.getId());
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant disApproveTheRestaurant(Long restId) {
        Restaurant restaurant = restaurantRepository.findById(restId).orElseThrow();
        if(restaurant.getStatus()== STATUS.NOT_APPROVED) {
            return restaurant;
        }
        restaurant.setStatus(STATUS.NOT_APPROVED);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public List<Dishes> getMenuByRestaurantName(String restsName) {
        return restaurantRepository.findByRestaurantName(restsName).orElseThrow().getMenu();
    }

    @Override
    public Restaurant addRatingToRest(String restName, Float ratings) {
        Restaurant restaurant = restaurantRepository.findByRestaurantName(restName).orElseThrow();
        Float newRating =
                ((((restaurant.getRatings() * restaurant.getCount()) + ratings) / (restaurant.getCount() + 1)));
        restaurant.setRatings(newRating);
        restaurant.setCount(restaurant.getCount()+1);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant getRestaurantByEmail(String email) {
        return restaurantRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public Optional<Restaurant> getRestaurantByName(String restsName) {
       return restaurantRepository.findByRestaurantName(restsName);
    }


}

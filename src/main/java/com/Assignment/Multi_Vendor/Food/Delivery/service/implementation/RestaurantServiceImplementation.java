package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.DishNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectInputException;
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
                .orElseThrow(()-> new RestaurantNotFoundException("No such Restaurant found, Pls enter valid restaurant id"));
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
    public Restaurant addNewMenu(List<Dishes> menu, Long restId) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findById                                                                                      (restId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

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
    public Restaurant addDishesToMenu(List<Dishes> dishes, Long restId)
            throws RestaurantNotFoundException, IncorrectInputException {
        Restaurant restaurant = restaurantRepository.findById(restId)
                .orElseThrow(
                        ()-> new RestaurantNotFoundException("No Such Resturant found")
                );
        if(dishes.isEmpty()){
            throw new IncorrectInputException("Pls enter at-least one Dish ");
        }
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
    public Restaurant removeDishFromMenu(String restName, String dishName) throws DishNotFoundException, RestaurantNotFoundException {
        Dishes dish = dishesRepository.findByNameAndRestaurant_RestaurantName(dishName, restName)
                .orElseThrow(()-> new DishNotFoundException("No such dish found, from specified restaurant"));
        Restaurant restaurant = restaurantRepository.findByRestaurantName(restName)
                .orElseThrow(()-> new RestaurantNotFoundException("No such Restaurant found"));
        restaurant.getMenu().remove(dish);
        dishesRepository.deleteById(dish.getId());
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant disApproveTheRestaurant(Long restId) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findById(restId)
                .orElseThrow(()-> new RestaurantNotFoundException("No such Restaurant found, Pls enter valid restaurant id"));
        if(restaurant.getStatus()== STATUS.NOT_APPROVED) {
            return restaurant;
        }
        restaurant.setStatus(STATUS.NOT_APPROVED);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public List<Dishes> getMenuByRestaurantName(String restsName) throws RestaurantNotFoundException {
        return restaurantRepository.findByRestaurantName(restsName)
                .orElseThrow(()-> new RestaurantNotFoundException("No such restaurant found"))
                .getMenu();
    }

    @Override
    public Restaurant addRatingToRest(String restName, Float ratings) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findByRestaurantName(restName)
                .orElseThrow(()-> new RestaurantNotFoundException("No such restaurant found"));
        Float newRating =
                ((((restaurant.getRatings() * restaurant.getCount()) + ratings) / (restaurant.getCount() + 1)));
        restaurant.setRatings(newRating);
        restaurant.setCount(restaurant.getCount()+1);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant getRestaurantByEmail(String email) throws RestaurantNotFoundException {
        return restaurantRepository.findByEmail(email)
                .orElseThrow(()-> new RestaurantNotFoundException("No such restaurant found"));
    }

    @Override
    public Restaurant getRestaurantByName(String restsName) throws RestaurantNotFoundException {
       return restaurantRepository.findByRestaurantName(restsName)
               .orElseThrow(()-> new RestaurantNotFoundException("No such restaurant found"));
    }

    @Override
    public Restaurant checkIfRestaurantExists(String email) throws RestaurantNotFoundException {
        return restaurantRepository.findByEmail(email)
                .orElseThrow(()-> new RestaurantNotFoundException("No such restaurant found."));
    }

    @Override
    public Restaurant checkIfRestaurantExistsAndApproved(String restsName) {
        return restaurantRepository.findByRestaurantNameAndStatus(restsName,STATUS.APPROVED);
    }


}

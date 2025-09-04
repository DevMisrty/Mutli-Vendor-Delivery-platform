package com.Assignment.Multi_Vendor.Food.Delivery.repository;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface DishesRepository extends ListCrudRepository<Dishes, Long> {
    Optional<List<Dishes>> findByCuisine(Cuisine cuisine);

    List<Dishes> findByRatingGreaterThanEqual(Float star);

    Optional<Dishes> findByNameAndRestaurant_Id(String name, Long restaurantId);

    Optional<Dishes> findByNameAndRestaurant_RestaurantName(String name, String restName);
}

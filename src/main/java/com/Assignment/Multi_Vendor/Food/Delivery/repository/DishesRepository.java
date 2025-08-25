package com.Assignment.Multi_Vendor.Food.Delivery.repository;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface DishesRepository extends ListCrudRepository<Dishes, Long> {
    List<Dishes> findByCuisine(Cuisine cuisine);

    List<Dishes> findByRatingGreaterThanEqual(Integer star);
}

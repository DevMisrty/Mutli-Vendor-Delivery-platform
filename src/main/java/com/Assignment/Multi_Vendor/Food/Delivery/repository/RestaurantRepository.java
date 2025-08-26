package com.Assignment.Multi_Vendor.Food.Delivery.repository;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.model.STATUS;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends ListCrudRepository<Restaurant,Long> {
    List<Restaurant> findAllByStatus(STATUS status);

    Optional<Restaurant> findByRestaurantName(String name);
}

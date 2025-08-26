package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.converter.PojoToDtoConverter;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DishesRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DishesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishesServiceImplementation implements DishesService {


    private final DishesRepository dishesRepository;
    private final PojoToDtoConverter converter;

    @Override
    public List<DishesResponseDto> getMenuBasedOnCuisine(Cuisine selectedCuisine) {
        List<Dishes> dishes = dishesRepository.findByCuisine(selectedCuisine);
        return dishes.stream()
                .filter(dish -> dish.getRestaurant()!=null)
                .map(converter::convertDishToDishResponseDto )
                .collect(Collectors.toList());
    }

    @Override
    public List<DishesResponseDto> getMenuBasedOnStar(Integer star) {
        List<Dishes> dishes = dishesRepository.findByRatingGreaterThanEqual(star);

        return dishes.stream()
                .filter(dish -> dish.getRestaurant()!=null)
                .map(converter::convertDishToDishResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Dishes addRatingsToDish(String restName, String dishName, Float star) {
        Dishes dish =
                dishesRepository.findByNameAndRestaurant_RestaurantName(dishName, restName)
                        .orElseThrow();
        log.info("Dish Details :{}", dish);
        Float newRating =
                (((dish.getCount() * dish.getRating()) + star ) / (dish.getCount() + 1) );
        dish.setCount(dish.getCount()+1);
        dish.setRating(newRating);

        log.info("new Rating: {}",newRating);
        return dishesRepository.save(dish);
    }
}

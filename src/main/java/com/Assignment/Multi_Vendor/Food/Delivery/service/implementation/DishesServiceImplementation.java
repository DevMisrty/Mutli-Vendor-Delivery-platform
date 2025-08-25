package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.converter.DishToDishResponseDtoConverter;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DishesRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DishesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishesServiceImplementation implements DishesService {


    private final DishesRepository dishesRepository;
    private final DishToDishResponseDtoConverter converter;

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
}

package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectInputException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.NoSuchCuisineFound;
import com.Assignment.Multi_Vendor.Food.Delivery.converter.PojoToDtoConverter;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.model.STATUS;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DishesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class DishesServiceImplementationTest {

    @Mock
    private DishesRepository dishesRepository;

    @Mock
    private PojoToDtoConverter converter;

    @InjectMocks
    private DishesServiceImplementation dishesService;

    private List<Dishes> dishes = new ArrayList<>();

    @BeforeEach
    public void setUp(){

        Restaurant restaurant1 =
                new Restaurant(1L,"rest1","owner1","root","owner1@rest1.com");
        Restaurant restaurant2 =
                new Restaurant(2L,"rest2","owner2","root","owner2@rest2.com");
        restaurant2.setStatus(STATUS.APPROVED);
        dishes.add(Dishes.builder()
                .id(1L)
                .name("Margherita Pizza")
                .description("Classic pizza with mozzarella, basil, and tomato sauce")
                .cuisine(Cuisine.ITALIAN)
                .price(299.0)
                .rating(0f)
                .count(0L)
                .restaurant(restaurant2) // can set restaurant if needed
                .build());

        dishes.add(Dishes.builder()
                .id(2L)
                .name("Paneer Butter Masala")
                .description("Paneer cooked in a rich creamy tomato gravy")
                .cuisine(Cuisine.INDIAN)
                .price(249.0)
                .rating(4.7f)
                .count(300L)
                .restaurant(restaurant1)
                .build());

        dishes.add(Dishes.builder()
                .id(3L)
                .name("Chicken Biryani")
                .description("Fragrant basmati rice cooked with spiced chicken")
                .cuisine(Cuisine.INDIAN)
                .price(349.0)
                .rating(4.6f)
                .count(500L)
                .restaurant(restaurant2)
                .build());

        dishes.add(Dishes.builder()
                .id(4L)
                .name("Sushi Platter")
                .description("Assorted sushi with soy sauce and wasabi")
                .cuisine(Cuisine.JAPANESE)
                .price(599.0)
                .rating(4.8f)
                .count(220L)
                .restaurant(null)
                .build());

        dishes.add(Dishes.builder()
                .id(5L)
                .name("Veg Hakka Noodles")
                .description("Stir-fried noodles with veggies and sauces")
                .cuisine(Cuisine.CHINESE)
                .price(199.0)
                .rating(4.3f)
                .count(180L)
                .restaurant(restaurant1)
                .build());
    }

    @Test
    public void getMenuBasedOnCuisine_test() throws NoSuchCuisineFound {

        List<Dishes> indianDishes = dishes.stream()
                .filter(dish -> dish.getCuisine().equals(Cuisine.INDIAN))
                .filter(dish -> dish.getRestaurant()!=null)
                .filter(dishes1 -> dishes1.getRestaurant().getStatus().equals(STATUS.APPROVED))
                .collect(Collectors.toList());

        List<Dishes> chineseDishes = dishes.stream()
                .filter(dish -> dish.getCuisine().equals(Cuisine.CHINESE))
                .collect(Collectors.toList());

        List<Dishes> japaneseDish = dishes.stream()
                .filter(dish -> dish.getCuisine().equals(Cuisine.JAPANESE))
                .collect(Collectors.toList());

        List<DishesResponseDto> resultantIndianDish = indianDishes.stream()
                .map(dish -> converter.convertDishToDishResponseDto(dish))
                .collect(Collectors.toList());

        List<DishesResponseDto> resultEmptyList = new ArrayList<>();

        when(dishesRepository.findByCuisine(Cuisine.INDIAN))
                .thenReturn(Optional.of(indianDishes));
        when(dishesRepository.findByCuisine(Cuisine.AMERICAN))
                .thenReturn(Optional.empty());
        when(dishesRepository.findByCuisine(Cuisine.CHINESE))
                .thenReturn(Optional.of(chineseDishes));
        when(dishesRepository.findByCuisine(Cuisine.JAPANESE))
                .thenReturn(Optional.of(japaneseDish));


        assertThrows(NoSuchCuisineFound.class, ()-> dishesService.getMenuBasedOnCuisine(Cuisine.AMERICAN));
        assertIterableEquals(resultantIndianDish,dishesService.getMenuBasedOnCuisine(Cuisine.INDIAN));
        assertIterableEquals(resultEmptyList, dishesService.getMenuBasedOnCuisine(Cuisine.CHINESE));
        assertIterableEquals(resultEmptyList, dishesService.getMenuBasedOnCuisine(Cuisine.JAPANESE));


    }

    @Test
    public void getMenuBasedOnStar_test() throws IncorrectInputException {
        List<Dishes> firstLevelStarDishes = dishes
                .stream()
                .filter(dish -> dish.getRating()>= 4.5f)
                .collect(Collectors.toList());

        List<DishesResponseDto> resultDishes = firstLevelStarDishes.stream()
                .filter(dish -> dish.getRestaurant()!=null)
                .filter(dish -> dish.getRestaurant().getStatus()!= STATUS.NOT_APPROVED)
                .map(converter::convertDishToDishResponseDto)
                .collect(Collectors.toList());

        when(dishesRepository.findByRatingGreaterThanEqual(4.5f))
                .thenReturn(firstLevelStarDishes);

        assertIterableEquals(resultDishes,dishesService.getMenuBasedOnStar(4.5f));
        assertThrows(IncorrectInputException.class,()-> dishesService.getMenuBasedOnStar(-1.5f));
        assertThrows(IncorrectInputException.class,()-> dishesService.getMenuBasedOnStar(7f));
    }

    @Test
    public void addRatingsToDish_test() {

        String restName = "restaurant1";
        String dishName = "Margherita Pizza";

        when(dishesRepository.findByNameAndRestaurant_RestaurantName(dishName,restName))
                .thenReturn(Optional.of(dishes.getFirst()));

        when(dishesRepository.save(dishes.get(0)))
                .thenReturn(dishes.get(0));

        Dishes resultDish = dishesService.addRatingsToDish(restName, dishName, 4.0f);
        Float excpected = 4.0f;
        assertEquals(excpected,resultDish.getRating());

        when(dishesRepository.findByNameAndRestaurant_RestaurantName(anyString(),anyString()))
                .thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                ()-> dishesService.addRatingsToDish(dishName,restName,1f));
    }
}

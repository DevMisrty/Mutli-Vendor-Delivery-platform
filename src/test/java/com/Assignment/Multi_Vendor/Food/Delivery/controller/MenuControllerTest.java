package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Cuisine;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.model.STATUS;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.implementation.DishesServiceImplementation;
import com.Assignment.Multi_Vendor.Food.Delivery.service.implementation.RestaurantServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


import com.Assignment.Multi_Vendor.Food.Delivery.model.ROLE;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {
    @Mock
    private RestaurantServiceImplementation restaurantService;
    @Mock
    private DishesServiceImplementation dishesService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private MenuController controller;

    Restaurant rest;
    List<Dishes> dishesList;
    List<Restaurant> restaurants;
    List<DishesResponseDto> dishesResponseList;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
         rest = Restaurant.builder()
                .id(1L)
                .restaurantName("Dominos")
                .ownerName("John Doe")
                .email("dominos@gmail.com")
                .password("secure123")
                .menu(null)                  // explicitly set menu as null
                .ratings(0f)
                .count(0L)
                .role(ROLE.RESTAURANT_OWNER)
                .status(STATUS.APPROVED)
                .build();
        dishesResponseList = List.of(
                DishesResponseDto.builder()
                        .restaurantName("Dominos")
                        .name("Margherita Pizza")
                        .description("Classic cheese pizza with fresh mozzarella")
                        .cuisine(Cuisine.ITALIAN)
                        .price(299.0)
                        .rating(0f)
                        .build(),

                DishesResponseDto.builder()
                        .restaurantName("Dominos")
                        .name("Paneer Tikka Pizza")
                        .description("Paneer tikka, capsicum, and cheese loaded pizza")
                        .cuisine(Cuisine.INDIAN)
                        .price(349.0)
                        .rating(0f)
                        .build(),

                DishesResponseDto.builder()
                        .restaurantName("Pizza Hut")
                        .name("Veggie Supreme")
                        .description("Loaded with capsicum, onion, and mushrooms")
                        .cuisine(Cuisine.ITALIAN)
                        .price(399.0)
                        .rating(0f)
                        .build()
        );

        dishesList = List.of(
                Dishes.builder()
                        .name("Margherita Pizza")
                        .description("Classic cheese pizza")
                        .cuisine(Cuisine.ITALIAN)
                        .price(299.0)
                        .rating(0f)    // explicitly set
                        .count(0L)
                        .restaurant(rest)// explicitly set
                        .build(),

                Dishes.builder()
                        .name("Paneer Butter Masala")
                        .description("Rich creamy paneer curry")
                        .cuisine(Cuisine.INDIAN)
                        .price(350.0)
                        .rating(0f)
                        .restaurant(rest)
                        .count(0L)
                        .build(),

                Dishes.builder()
                        .name("Veg Sushi Roll")
                        .description("Japanese veg sushi roll")
                        .cuisine(Cuisine.JAPANESE)
                        .price(450.0)
                        .rating(0f)
                        .count(0L)
                        .build()
        );

        restaurants = List.of(
                Restaurant.builder()
                        .id(1L)
                        .restaurantName("Dominos")
                        .ownerName("John Doe")
                        .email("dominos@gmail.com")
                        .password("secure123")
                        .menu(null)                  // explicitly set menu as null
                        .ratings(0f)
                        .count(0L)
                        .role(ROLE.RESTAURANT_OWNER)
                        .status(STATUS.APPROVED)
                        .build(),

                Restaurant.builder()
                        .id(2L)
                        .restaurantName("Pizza Hut")
                        .ownerName("Jane Smith")
                        .email("pizzahut@gmail.com")
                        .password("pass456")
                        .menu(null)                  // explicitly set menu as null
                        .ratings(0f)
                        .count(0L)
                        .role(ROLE.RESTAURANT_OWNER)
                        .status(STATUS.NOT_APPROVED)
                        .build(),

                Restaurant.builder()
                        .id(3L)
                        .restaurantName("KFC")
                        .ownerName("Kevin Brown")
                        .email("kfc@gmail.com")
                        .password("kfc123")
                        .menu(null)
                        .ratings(0f)
                        .count(0L)
                        .role(ROLE.RESTAURANT_OWNER)
                        .status(STATUS.NOT_APPROVED)
                        .build()
        );
    }

    @Test
    public void getMenuOfRestaurant_Throws_Exception_when_NoRestFound() throws Exception {
        Restaurant rest = restaurants.getFirst();
        when(restaurantService.checkIfRestaurantExistsAndApproved(anyString()))
                .thenReturn(null);
        assertThrows( RestaurantNotFoundException.class,
                () -> controller.getMenuOfRestaurant(rest.getRestaurantName()));
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void getMenuOfRestaurant_Expected_test() throws Exception {
        when(restaurantService.checkIfRestaurantExistsAndApproved(anyString()))
                .thenReturn(rest);
        when(restaurantService.getMenuByRestaurantName(anyString()))
                .thenReturn(dishesList);

        mockMvc.perform(get("/menu/rest/rest1").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(200),
                        jsonPath("$.data",hasSize(dishesList.size())),
                        jsonPath("$.data[0]").value(dishesList.getFirst()),
                        jsonPath("$.data[1]").value(dishesList.get(1))
                );
    }

}
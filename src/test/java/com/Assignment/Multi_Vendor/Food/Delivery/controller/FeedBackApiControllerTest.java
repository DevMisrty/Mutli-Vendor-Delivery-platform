package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.converter.PojoToDtoConverter;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.RestaurantResponseDTO;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DishesService;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FeedBackApiControllerTest {

    @MockitoBean
    private RestaurantServiceImplementation restaurantService;

    @MockitoBean
    private DishesServiceImplementation dishesService;

    @MockitoBean
    private ModelMapper modelMapper;

    @MockitoBean
    private PojoToDtoConverter converter;

    @Autowired
    private FeedBackApiController controller;

    @Autowired
    private MockMvc mockMvc;

    private Dishes dish;
    private DishesResponseDto dishResponseDto;
    private Restaurant restaurant;
    private RestaurantResponseDTO restaurantResponseDTO;


    @BeforeEach
    public void setup(){

        dish = new Dishes();
        dish.setId(1L);
        dish.setName("Paneer Tikka");
        dish.setRating(0f);
        dish.setCount(0L);


        dishResponseDto = new DishesResponseDto();
        dishResponseDto.setName("Paneer Tikka");
        dishResponseDto.setRating(4.5f);



        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setRestaurantName("Spice Villa");
        restaurant.setRatings(4.8f);
        restaurant.setMenu(new ArrayList<>(List.of(dish)));
        dish.setRestaurant(restaurant);
        dishResponseDto.setRestaurantName(restaurant.getRestaurantName());

        restaurantResponseDTO = new RestaurantResponseDTO();
        restaurantResponseDTO.setId(1L);
        restaurantResponseDTO.setRestaurantName("Spice Villa");
        restaurantResponseDTO.setRatings(4.8f);
    }


    @Test
    @WithMockUser(username = "user", roles = {"CUSTOMER"})
    public void giveFeedbackToDish_test() throws Exception {

        String restName = restaurant.getRestaurantName();
        String dishName = dish.getName();

        when(dishesService.addRatingsToDish(anyString(), anyString(),anyFloat()))
                .thenReturn(dish);
        when(converter.convertDishToDishResponseDto(any(Dishes.class)))
                .thenReturn(dishResponseDto);

        mockMvc.perform(get("/feedback/dish/" + restName + "/" + dishName + "/4.5")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.name").value("Paneer Tikka"))
                .andExpect(jsonPath("$.data.rating").value(4.5f));


        dishResponseDto.setRating(5f);
        mockMvc.perform(get("/feedback/dish/" + restName +"/"+dishName +"/7.8")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.name").value("Paneer Tikka"))
                .andExpect(jsonPath("$.data.rating").value(5f));
    }

    @Test
    @WithMockUser(username = "user", roles = {"CUSTOMER"})
    public void giveFeedbackToRestaurant_test() throws Exception {

        String restName = restaurant.getRestaurantName();

        when(restaurantService.addRatingToRest(anyString(),anyFloat()))
                .thenReturn(restaurant);

        when(modelMapper.map(restaurant,RestaurantResponseDTO.class))
                .thenReturn(restaurantResponseDTO);

        mockMvc.perform(get("/feedback/rest/"+restName+"/4.5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.restaurantName").value(restName))
                .andExpect(jsonPath("$.data.ratings").value(4.8f));

        mockMvc.perform(get("/feedback/rest/"+restName+"/4.5").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.status").value(201),
                        jsonPath("$.data.ratings").value(4.8f),
                        jsonPath("$.data.restaurantName").value(restName)
                );

    }
}
package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.AdminRestaurantResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.ROLE;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.model.STATUS;
import com.Assignment.Multi_Vendor.Food.Delivery.service.implementation.RestaurantServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @MockitoBean
    private RestaurantServiceImplementation restaurantService;

    @MockitoBean
    private ModelMapper modelMapper;

    @Autowired
    private AdminController adminController;

    List<AdminRestaurantResponseDto> restaurantResponseDto;
    List<Restaurant> restaurants= new ArrayList<>();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JobDetail statusChangeDetail;

    @BeforeEach
    public void setUp(){
        restaurantResponseDto = List.of(
                AdminRestaurantResponseDto.builder()
                        .id(1L)
                        .email("spicevilla@gmail.com")
                        .restaurantName("Spice Villa")
                        .ownerName("Ravi Sharma")
                        .status(STATUS.APPROVED)
                        .build());
        restaurants.add(Restaurant.builder()
                .id(1L)
                .restaurantName("Spice Villa")
                .ownerName("Ravi Sharma")
                .email("spicevilla@gmail.com")
                .password("password123")
                .ratings(4.6f)
                .count(120L)
                .status(STATUS.APPROVED)
                .role(ROLE.RESTAURANT_OWNER)
                .menu(new ArrayList<>()) // dishes will be added later
                .build()
        );
        restaurants.add(Restaurant.builder()
                .id(2L)
                .restaurantName("Food Villa")
                .ownerName("Ravi Kishn")
                .email("foodVilla@gmail.com")
                .password("password123")
                .ratings(4.6f)
                .count(120L)
                .status(STATUS.NOT_APPROVED)
                .role(ROLE.RESTAURANT_OWNER)
                .menu(new ArrayList<>()) // dishes will be added later
                .build()
        );

        Restaurant spiceVilla = Restaurant.builder()
                .id(1L)
                .restaurantName("Spice Villa")
                .ownerName("Ravi Sharma")
                .email("spicevilla@gmail.com")
                .password("pass123")
                .ratings(4.8f)
                .status(STATUS.APPROVED)
                .build();

        Dishes paneerTikka = Dishes.builder()
                .id(1L)
                .name("Paneer Tikka")
                .price(250.0)
                .rating(4.5f)
                .count(120L)
                .restaurant(spiceVilla)
                .build();

        Dishes butterNaan = Dishes.builder()
                .id(2L)
                .name("Butter Naan")
                .price(50.0)
                .rating(4.7f)
                .count(300L)
                .restaurant(spiceVilla)
                .build();
        spiceVilla.setMenu(List.of(paneerTikka, butterNaan));
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void getListOfRestaurantToApprove_Test() throws Exception {
        when(modelMapper.map(any(Restaurant.class), eq(AdminRestaurantResponseDto.class)))
                .thenReturn(restaurantResponseDto.getFirst());
        when(restaurantService.getAllNotApprovedRestaurant())
                .thenReturn(restaurants);

        mockMvc.perform(get("/admin/toApprove").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(200),
                        jsonPath("$.data[0]").value(restaurantResponseDto.getFirst())
                );

        when(modelMapper.map(any(Restaurant.class), eq(AdminRestaurantResponseDto.class)))
                .thenReturn(null);
        when(restaurantService.getAllNotApprovedRestaurant())
                .thenReturn(restaurants);


        mockMvc.perform(get("/admin/toApprove").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(200),
                        jsonPath("$.data",hasSize(1))
                );
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void approveTheRestaurant_test() throws Exception {
        Restaurant rest = restaurants.getLast();
        rest.setStatus(STATUS.APPROVED);
        AdminRestaurantResponseDto responseDto = AdminRestaurantResponseDto.builder()
                .id(2L)
                .email("foodVilla@gmail.com")
                .restaurantName("Food Villa")
                .ownerName("Ravi Kishn")
                .status(STATUS.APPROVED)
                .build();

        when(restaurantService.approvedRestaurant(rest.getId()))
                .thenReturn(rest);
        when(modelMapper.map(rest, AdminRestaurantResponseDto.class))
                .thenReturn(responseDto);

        mockMvc.perform(get("/admin/approved/2").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isAccepted(),
                        jsonPath("$.data").value(responseDto)
                );
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void disapproveTheRestaurant_test() throws Exception {
        Restaurant rest = restaurants.getLast();
        AdminRestaurantResponseDto responseDto = AdminRestaurantResponseDto.builder()
                .id(2L)
                .email("foodVilla@gmail.com")
                .restaurantName("Food Villa")
                .ownerName("Ravi Kishn")
                .status(STATUS.NOT_APPROVED)
                .build();

        when(modelMapper.map(rest, AdminRestaurantResponseDto.class))
                .thenReturn(responseDto);
        when(restaurantService.disApproveTheRestaurant(anyLong()))
                .thenReturn(rest);

        mockMvc.perform(get("/admin/disapproved/1").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isAccepted(),
                        jsonPath("$.data").value(responseDto)
                );
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void getAllRestaurant_test() throws Exception {
        Restaurant rest = restaurants.getFirst();
        List<AdminRestaurantResponseDto> responseDto = Arrays.asList(
                AdminRestaurantResponseDto.builder()
                        .id(1L)
                        .email("spicevilla@gmail.com")
                        .restaurantName("Spice Villa")
                        .ownerName("Ravi Sharma")
                        .status(STATUS.APPROVED)
                        .build(),
                AdminRestaurantResponseDto.builder()
                        .id(2L)
                        .email("foodVilla@gmail.com")
                        .restaurantName("Food Villa")
                        .ownerName("Ravi Kishn")
                        .status(STATUS.NOT_APPROVED)
                        .build()
        );

        when(modelMapper.map(rest, AdminRestaurantResponseDto.class))
                .thenReturn(responseDto.getFirst());
        when(restaurantService.getAllRestaurant())
                .thenReturn(restaurants);

        mockMvc.perform(get("/admin/allRestaurants").contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data",hasSize(restaurants.size())),
                        jsonPath("$.data[0]").value(responseDto.getFirst())
                );
    }

}
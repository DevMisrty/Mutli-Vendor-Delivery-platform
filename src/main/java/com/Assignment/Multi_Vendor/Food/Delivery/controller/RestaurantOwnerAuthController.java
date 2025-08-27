package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.JWt.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.*;
import com.Assignment.Multi_Vendor.Food.Delivery.model.ROLE;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.RestaurantRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class RestaurantOwnerAuthController {


    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager ownerAuthenticateManager;
    private final RestaurantService restaurantService;
    private final JwtUtility jwtUtility;

    @PostMapping("/rest/signin")
    public ResponseEntity<ApiResponse<?>> addNewRestaurantOwner(@RequestBody RestaurantOwnerDto restaurantOwnerDto){
        Restaurant rest = modelMapper.map(restaurantOwnerDto, Restaurant.class);
        rest.setPassword(passwordEncoder.encode(rest.getPassword()));
        rest.setRole(ROLE.RESTAURANT_OWNER);
        RestaurantOwnerDto response =
                modelMapper.map(restaurantService.addNewRestaurant(rest), RestaurantOwnerDto.class);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "your account has been created, pls wait for admin to approve ur restaurant",
                        response
                ));
    }

    @PostMapping("/rest/login")
    public ResponseEntity<ApiResponse<?>> authenticateRestaurantOwner(@RequestBody LoginRequestDto requestDto){
        ownerAuthenticateManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword()
                )
        );

        Restaurant rest = restaurantService.getRestaurantByEmail(requestDto.getEmail());

        Users users = Users.builder()
                .email(rest.getEmail())
                .role(rest.getRole().toString())
                .build();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>(
                        HttpStatus.ACCEPTED.value(),
                        "U have successfully authenticated",
                        jwtUtility.generateAccessToken(users)
                ));
    }
}

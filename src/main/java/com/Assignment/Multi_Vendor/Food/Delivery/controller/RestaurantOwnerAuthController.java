package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectCredentialsException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.JWT.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.configuration.FoodDeliveryPlatform;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.*;
import com.Assignment.Multi_Vendor.Food.Delivery.model.ROLE;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.implementation.OTPAuthService;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.ApiResponseGenerator;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.MessageConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class RestaurantOwnerAuthController {


    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager ownerAuthenticateManager;
    private final RestaurantService restaurantService;
    private final JwtUtility jwtUtility;
    private final OTPAuthService oTPAuthService;

    private Integer otp;
    private String email;

    @PostMapping("/rest/signin")
    public ResponseEntity<ApiResponse<RestaurantOwnerDto>> addNewRestaurantOwner(
            @Valid @RequestBody RestaurantOwnerDto restaurantOwnerDto)
            throws RestaurantNameAlreadyTakenException {
        Restaurant rest = modelMapper.map(restaurantOwnerDto, Restaurant.class);
        rest.setPassword(passwordEncoder.encode(rest.getPassword()));
        rest.setRole(ROLE.RESTAURANT_OWNER);
        RestaurantOwnerDto response =
                modelMapper.map(restaurantService.addNewRestaurant(rest), RestaurantOwnerDto.class);

        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.OK,
                        MessageConstants.RESTAURANT_CREATED,
                        response
                );
    }

    @PostMapping("/rest/login")
    public ResponseEntity<ApiResponse<String>> authenticateRestaurantOwner(
            @Valid @RequestBody LoginRequestDto requestDto){
        try{
            ownerAuthenticateManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword()
                    )
            );
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Invalid Credentials, pls try again with proper credentials. "
                    ));
        }

        EmailDetailsDto emailDetails = EmailDetailsDto.builder()
                .to(requestDto.getEmail())
                .build();

        email = requestDto.getEmail();
        otp = FoodDeliveryPlatform.generateOtp();

        oTPAuthService.sendMail(emailDetails, otp);
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.ACCEPTED,
                        MessageConstants.OTP_SENT,
                        MessageConstants.OTP_SENT
                );
    }


    @PostMapping("/rest/otpverification")
    public ResponseEntity<ApiResponse<String>> getOtpVerify(@Valid @RequestBody OtpRequestDto requestDto) throws IncorrectCredentialsException, RestaurantNotFoundException {
        if(!requestDto.getOtp().equals(otp) || !email.equals(requestDto.getEmail())){
            throw new IncorrectCredentialsException("pls provide correct information. ");
        }

        Restaurant rest = restaurantService.getRestaurantByEmail(requestDto.getEmail());



        Users users = Users.builder()
                .email(rest.getEmail())
                .role(rest.getRole().toString())
                .build();

        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.ACCEPTED,
                        MessageConstants.OTP_VERIFIED,
                        jwtUtility.generateAccessToken(users)
                );
    }
}

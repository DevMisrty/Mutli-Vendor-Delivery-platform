package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectCredentialsException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.UserNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.JWT.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.configuration.FoodDeliveryPlatform;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.*;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.CustomerService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.implementation.OTPAuthService;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.ApiResponseGenerator;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.MessageConstants;
import jakarta.mail.Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/customer")
@RequiredArgsConstructor
public class CustomerAuthController {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final AuthenticationManager customerAuthenticationManager;
    private final CustomerRepository customerRepository;
    private final JwtUtility jwtUtility;
    private final OTPAuthService oTPAuthService;
    private Integer otp;
    private String email;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<CustomerDto>> addNewCustomer(@Valid @RequestBody CustomerDto customerDto)
            throws UserNameAlreadyTakenException {
        Customers customer = modelMapper.map(customerDto, Customers.class);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customers customers = customerService.addNewCustomer(customer);
        CustomerDto responseDto = modelMapper.map(customers, CustomerDto.class);
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(HttpStatus.OK,
                        MessageConstants.USER_CREATED,
                        responseDto
                );
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@Valid @RequestBody LoginRequestDto loginRequestDto){

        try {
            customerAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );

            EmailDetailsDto emailDetails = EmailDetailsDto.builder()
                    .to(loginRequestDto.getEmail())
                    .build();

            email= loginRequestDto.getEmail();
            otp = FoodDeliveryPlatform.generateOtp();

            oTPAuthService.sendMail(emailDetails, otp);
            return ApiResponseGenerator
                    .generateSuccessfulApiResponse(HttpStatus.OK,
                            MessageConstants.USER_CREATED,
                            MessageConstants.OTP_SENT
                    );
        }
        catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageConstants.INVALID_CREDENTIALS);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageConstants.USER_NOT_FOUND);
        }
    }

    @PostMapping("/otpverification")
    public ResponseEntity<ApiResponse<String>> otpVerification(@Valid @RequestBody OtpRequestDto requestDto)
            throws IncorrectCredentialsException {

        if(!requestDto.getOtp().equals(otp) || !email.equals(requestDto.getEmail())){
 
            throw new IncorrectCredentialsException(MessageConstants.OTP_INVALID);
        }

        Customers customer = customerRepository.findByEmail(requestDto.getEmail()).orElseThrow();

        Users users = Users.builder()
                .email(customer.getEmail())
                .role(customer.getRole().toString())
                .build();
        String token = jwtUtility.generateAccessToken(users);
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(HttpStatus.OK,
                        MessageConstants.USER_CREATED,
                        token
                );
    }
}

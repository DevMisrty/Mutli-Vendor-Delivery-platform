package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.JWt.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.*;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.CustomerService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.implementation.OTPAuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/customer")
@RequiredArgsConstructor
public class CustomerAuthController {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;

    @Autowired
    private AuthenticationManager customerAuthenticationManager;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private OTPAuthService oTPAuthService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<CustomerDto>> addNewCustomer(@RequestBody CustomerDto customerDto){
        Customers customer = modelMapper.map(customerDto, Customers.class);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customers customers = customerService.addNewCustomer(customer);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Your Account has been created",
                        modelMapper.map(customers, CustomerDto.class)
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginCustomer(@RequestBody LoginRequestDto loginRequestDto){
        customerAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        EmailDetailsDto emailDetails = EmailDetailsDto.builder()
                .to(loginRequestDto.getEmail())
                .build();

        oTPAuthService.sendMail(emailDetails);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "your credentials has been verified, and mail has been send to your email for verification",
                        "redirect to localhost:8080/auth/customer/otpverification , for verifing the otp"
                ));
    }

    @PostMapping("/otpverification")
    public ResponseEntity<ApiResponse<?>> otpVerification(@RequestBody OtpRequestDto requestDto){

        if(requestDto.getOtp() != 123456){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Provided otp is incorrect, pls login again"
                    ));
        }

        Customers customer = customerRepository.findByEmail(requestDto.getEmail()).orElseThrow();

        Users users = Users.builder()
                .email(customer.getEmail())
                .role(customer.getRole().toString())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "your credentials has been verified",
                        jwtUtility.generateAccessToken(users)
                ));
    }
}

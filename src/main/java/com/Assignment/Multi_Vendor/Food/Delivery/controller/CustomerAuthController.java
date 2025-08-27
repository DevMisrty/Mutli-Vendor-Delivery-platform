package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.JWt.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.CustomerDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.LoginRequestDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.Users;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.CustomerService;
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
    private EntityManagerFactoryInfo entityManagerFactoryInfo;
    @Autowired
    private JwtUtility jwtUtility;

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

        Customers customer = customerRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow();

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

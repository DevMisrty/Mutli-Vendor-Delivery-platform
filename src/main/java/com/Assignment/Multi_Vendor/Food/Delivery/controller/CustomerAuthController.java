package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectCredentialsException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.UserNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.JWt.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.configuration.FoodDeliveryPlatform;
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
    public ResponseEntity<ApiResponse<CustomerDto>> addNewCustomer(@RequestBody CustomerDto customerDto)
            throws UserNameAlreadyTakenException {
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
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequestDto loginRequestDto){

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

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(
                            HttpStatus.OK.value(),
                            "your credentials has been verified, and mail has been send to your email for verification",
                            "redirect to localhost:8080/auth/customer/otpverification , for verifing the otp"
                    ));
        }
        catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        } catch (DisabledException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Account is disabled");
        } catch (LockedException ex) {
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body("Account is locked");
        }
    }

    @PostMapping("/otpverification")
    public ResponseEntity<ApiResponse<?>> otpVerification(@RequestBody OtpRequestDto requestDto)
            throws IncorrectCredentialsException {

        if(!requestDto.getOtp().equals(otp) || !email.equals(requestDto.getEmail())){

            throw new IncorrectCredentialsException("Incorrect OTP, Pls enter correct otp.");
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

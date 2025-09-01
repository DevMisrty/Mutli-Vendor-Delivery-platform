package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.UserNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.JWt.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.LoginRequestDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.Users;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Admin;
import com.Assignment.Multi_Vendor.Food.Delivery.model.ROLE;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthenticationManager adminAuthenticationManager;
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EntityManagerFactoryInfo entityManagerFactoryInfo;
    private final JwtUtility jwtUtility;

    // for creating the admin entity inside the database, to get proper salt value for the BCrypt encoder.
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> addNewAdmin(@RequestBody LoginRequestDto requestDto) throws UserNameAlreadyTakenException {

        Admin admin = modelMapper.map(requestDto, Admin.class);
        admin.setRole(ROLE.ADMIN);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        if (adminRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserNameAlreadyTakenException();
        }
        adminRepository.save(admin);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Ur account has been created",
                        "U have successfully registered here"
                ));

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAdmin(@RequestBody LoginRequestDto loginRequestDto) {
        try{
            Authentication authenticate = adminAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );
            Admin admin = adminRepository.findByEmail(loginRequestDto.getEmail())
                    .orElseThrow();
            Users users = Users.builder()
                    .email(admin.getEmail())
                    .role(admin.getRole().toString())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(
                            HttpStatus.ACCEPTED.value(),
                            "u have successfully login",
                            jwtUtility.generateAccessToken(users)
                    ));


        }catch (BadCredentialsException ex) {
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
}

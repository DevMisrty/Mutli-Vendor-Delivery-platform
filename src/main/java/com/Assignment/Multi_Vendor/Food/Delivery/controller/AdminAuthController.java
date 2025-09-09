package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.UserNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.JWT.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.LoginRequestDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.Users;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Admin;
import com.Assignment.Multi_Vendor.Food.Delivery.model.ROLE;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.AdminRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.ApiResponseGenerator;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.MessageConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
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
    private final JwtUtility jwtUtility;

    /**
     * Registers a new admin in the system.
     *
     * @param requestDto The DTO containing admin registration details
     * @return ResponseEntity containing the registration status
     * @throws UserNameAlreadyTakenException if an admin with the given email already exists
     *
     * @apiNote This endpoint is used to create a new admin account. The password will be
     *          automatically encoded before storing in the database.
     *
     * @example POST /auth/admin/signin
     *          Request Body: {"email": "admin@example.com", "password": "secure123"}
     */
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<String>> addNewAdmin( @Valid @RequestBody LoginRequestDto requestDto) throws UserNameAlreadyTakenException {

        Admin admin = modelMapper.map(requestDto, Admin.class);
        admin.setRole(ROLE.ADMIN);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        if (adminRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserNameAlreadyTakenException();
        }
        adminRepository.save(admin);
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(HttpStatus.OK, MessageConstants.CREATED, MessageConstants.CREATED);

    }

    /**
     * Authenticates an admin and returns a JWT token upon successful authentication.
     *
     * @param loginRequestDto The DTO containing login credentials
     * @return ResponseEntity containing the JWT token if authentication is successful
     *
     * @throws UsernameNotFoundException if the admin with the given email is not found
     * @throws BadCredentialsException if the provided credentials are invalid
     *
     * @apiNote This endpoint validates the admin's credentials and returns a JWT token
     *          that can be used for subsequent authenticated requests.
     *
     * @example POST /auth/admin/login
     *          Request Body: {"email": "admin@example.com", "password": "secure123"}
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateAdmin( @Valid  @RequestBody LoginRequestDto loginRequestDto) {
        try{
            adminAuthenticationManager.authenticate(
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

            String token = jwtUtility.generateAccessToken(users);
            return ApiResponseGenerator
                    .generateSuccessfulApiResponse(HttpStatus.ACCEPTED, MessageConstants.LOGIN_SUCCESS, token );


        }catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageConstants.INVALID_CREDENTIALS);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageConstants.USER_NOT_FOUND);
        }
    }
}
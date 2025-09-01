package com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectCredentialsException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectInputException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.UserNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.UserNameNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerGlobalExceptionHandler {


    @ExceptionHandler(UserNameAlreadyTakenException.class)
    public ResponseEntity<ApiResponse<?>> UsernameAlreadyTaken(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Username is already taken, pls use some other username. "
                ));
    }

    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> UsernameNotFound(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Username has not found, pls enter the correct credentials. "
                ));
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> IncorrectCredentials(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Incorrect Credentials, pls enter correct credentials and try again. "
                ));
    }

    @ExceptionHandler(IncorrectInputException.class)
    public ResponseEntity<ApiResponse<?>> IncorrectInputException(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiResponse<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Incorrect input, pls provide correct information"
                        )
                );
    }
}

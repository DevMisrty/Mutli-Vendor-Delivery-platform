package com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.CustomerAccessDeniedException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.CustomerNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerControllerExceptionHandler {

    @ExceptionHandler(CustomerAccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> customerAccessDenied(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiResponse<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Sorry, you dont have access for this action"
                        )
                );
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> customerNotFound(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiResponse<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "No Customer found, pls provide correct information"
                        )
                );
    }
}

package com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.NoSuchOrderException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderControllerExceptionHandler {

    @ExceptionHandler(NoSuchOrderException.class)
    public ResponseEntity<ApiResponse<?>> noSuchOrderException(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiResponse<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "No such order exists, from specified restaurant"
                        )
                );
    }

}

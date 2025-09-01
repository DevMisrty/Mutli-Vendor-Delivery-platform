package com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.DishNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.NoSuchCuisineFound;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DishesExceptionalHandler {

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> dishNotFoundExceptionHandler(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiResponse<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "No such dish exists, from specified restaurant"
                        )
                );
    }

    @ExceptionHandler(NoSuchCuisineFound.class)
    public ResponseEntity<ApiResponse<?>> noSuchCuisineFoundExceptionHandler(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiResponse<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "No such cuisine is found"
                        )
                );
    }


}

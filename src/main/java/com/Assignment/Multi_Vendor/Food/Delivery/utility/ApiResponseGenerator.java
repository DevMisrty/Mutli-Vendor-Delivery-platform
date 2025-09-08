package com.Assignment.Multi_Vendor.Food.Delivery.utility;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseGenerator {

    public static <T> ResponseEntity<ApiResponse<T>> generateSuccessfulApiResponse(
            HttpStatus status, String message, T data){
        return ResponseEntity
                .status(status)
                .body(new ApiResponse<>(
                        status.value(),
                        message,
                        (T) data
                ));
    }
}

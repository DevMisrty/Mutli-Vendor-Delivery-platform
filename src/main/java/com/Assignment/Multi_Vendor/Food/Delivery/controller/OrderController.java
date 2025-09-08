package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.*;
import com.Assignment.Multi_Vendor.Food.Delivery.JWT.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.OrdersService;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.ApiResponseGenerator;
import com.Assignment.Multi_Vendor.Food.Delivery.utility.MessageConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;
    private final JwtUtility jwtUtility;
    private final CustomerRepository customerRepository;


    // Places the order with restId, and DishId,
    @GetMapping("/placeOrder/{restName}/{dishName}")
    @Transactional
    public ResponseEntity<ApiResponse<OrderResponseDto>> placeOrder(
            @PathVariable String restName, 
            @PathVariable String dishName,
            HttpServletRequest request
            ) throws CustomerNotFoundException, DishNotFoundException, RestaurantNotFoundException {

        String token = request.getHeader("Authorization").substring(7);
        String emailFromToken = jwtUtility.getEmailFromToken(token);

        Customers customer = customerRepository.findByEmail(emailFromToken)
                .orElseThrow(
                        () -> new CustomerNotFoundException("No such customer found")
                );

        OrderResponseDto orderPlaced = ordersService.placeOrder(restName, dishName,customer);
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.ACCEPTED,
                        MessageConstants.ORDER_PLACED,
                        orderPlaced
                );
    }

    @GetMapping("/view/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> viewOrderDetails
            (@PathVariable Long orderId,
             HttpServletRequest request)
            throws CustomerNotFoundException, CustomerAccessDeniedException, NoSuchOrderException {

        String token = request.getHeader("Authorization").substring(7);
        String emailFromToken = jwtUtility.getEmailFromToken(token);

        Customers customer = customerRepository.findByEmail(emailFromToken)
                .orElseThrow(
                        ()-> new CustomerNotFoundException("No such customer found")
                );

        OrderResponseDto order = ordersService.viewOrderDetails(orderId, customer.getId());
        return ApiResponseGenerator
                .generateSuccessfulApiResponse(
                        HttpStatus.OK,
                        MessageConstants.ORDER_DETAILS,
                        order
                );
    }
}

package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.JWt.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.OrdersService;
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
    // pending completed - after applying security, fetch the customer id form the Jwt token, load it from
    //      customer repository, and add it to Order Entity, Currently, hard placed the value.
    @GetMapping("/placeOrder/{restName}/{dishName}")
    @Transactional
    public ResponseEntity<ApiResponse<OrderResponseDto>> placeOrder(
            @PathVariable String restName, 
            @PathVariable String dishName,
            HttpServletRequest request
            ){

        String token = request.getHeader("Authorization").substring(7);
        String emailFromToken = jwtUtility.getEmailFromToken(token);

        Customers customer = customerRepository.findByEmail(emailFromToken).orElseThrow();

        OrderResponseDto orderPlaced = ordersService.placeOrder(restName, dishName,customer);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>(
                        HttpStatus.ACCEPTED.value(),
                        "Order has been placed, with order id as " + orderPlaced.getOrderId(),
                        orderPlaced
                ));
    }

    @GetMapping("/view/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> viewOrderDetails(@PathVariable Long orderId, HttpServletRequest request){

        String token = request.getHeader("Authorization").substring(7);
        String emailFromToken = jwtUtility.getEmailFromToken(token);

        Customers customer = customerRepository.findByEmail(emailFromToken).orElseThrow();

        OrderResponseDto order = ordersService.viewOrderDetails(orderId, customer.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Details of the order "+ order.getOrderId(),
                        order
                ));
    }
}

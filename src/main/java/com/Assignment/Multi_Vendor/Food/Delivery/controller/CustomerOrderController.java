package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.JWt.JwtUtility;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.RestaurantRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DeliveryAgentService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.OrdersService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/order")
public class CustomerOrderController {

    private final OrdersService ordersService;
    private final DeliveryAgentService deliveryAgentService;
    private final JwtUtility jwtUtility;
    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;

    // changes the status of the order
    @GetMapping("/{orderId}/{status}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> changeOrderStatus(
            @PathVariable Long orderId,
            @PathVariable String status,
            HttpServletRequest request
            ){

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtility.getEmailFromToken(token);

        Restaurant restaurant = restaurantRepository.findByEmail(email).orElseThrow();

        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        OrderResponseDto orderResponse = ordersService.changeOrderStatus(orderId, orderStatus, restaurant.getRestaurantName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Order Details, after order status changed",
                        orderResponse
                ));
    }
}

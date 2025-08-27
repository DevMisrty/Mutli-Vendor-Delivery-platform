package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DeliveryAgentService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.OrdersService;
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

    // changes the status of the order
    @GetMapping("/{orderId}/{status}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> changeOrderStatus(@PathVariable Long orderId, @PathVariable String status ){
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        OrderResponseDto orderResponse = ordersService.changeOrderStatus(orderId, orderStatus);
        if(orderStatus.equals(OrderStatus.OUT_FOR_DELIVERY)){
            deliveryAgentService.assignDeliveryAgent(orderId);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Order Details, after order status changed",
                        orderResponse
                ));
    }
}

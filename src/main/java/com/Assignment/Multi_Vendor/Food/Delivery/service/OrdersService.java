package com.Assignment.Multi_Vendor.Food.Delivery.service;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;

public interface OrdersService {
    OrderResponseDto placeOrder(String restName, String dishName);

    OrderResponseDto viewOrderDetails(Long orderId);

    OrderResponseDto changeOrderStatus(Long orderId, OrderStatus orderStatus);
}

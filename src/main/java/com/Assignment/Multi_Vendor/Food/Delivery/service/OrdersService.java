package com.Assignment.Multi_Vendor.Food.Delivery.service;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import java.util.*;

public interface OrdersService {
    OrderResponseDto placeOrder(String restName, String dishName);

    OrderResponseDto viewOrderDetails(Long orderId);

    OrderResponseDto changeOrderStatus(Long orderId, OrderStatus orderStatus);

    List<Orders> getAllOutForDeliveryOrders(Integer limit);
}

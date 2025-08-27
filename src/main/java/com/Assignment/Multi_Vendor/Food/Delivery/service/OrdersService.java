package com.Assignment.Multi_Vendor.Food.Delivery.service;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import java.util.*;

public interface OrdersService {
    OrderResponseDto placeOrder(String restName, String dishName, Customers customers);

    OrderResponseDto viewOrderDetails(Long orderId, Long customerId);

    OrderResponseDto changeOrderStatus(Long orderId, OrderStatus orderStatus, String restName);

    List<Orders> getAllOutForDeliveryOrders(Integer limit);
}

package com.Assignment.Multi_Vendor.Food.Delivery.service;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.CustomerAccessDeniedException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.DishNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.NoSuchOrderException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantAccessDeniedException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import java.util.*;

public interface OrdersService {
    OrderResponseDto placeOrder(String restName, String dishName, Customers customers) throws DishNotFoundException;

    OrderResponseDto viewOrderDetails(Long orderId, Long customerId) throws CustomerAccessDeniedException;

    OrderResponseDto changeOrderStatus(Long orderId, OrderStatus orderStatus, String restName) throws NoSuchOrderException, RestaurantAccessDeniedException;

    List<Orders> getAllOutForDeliveryOrders(Integer limit);
}

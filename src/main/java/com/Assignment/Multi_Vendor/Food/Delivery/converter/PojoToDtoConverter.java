package com.Assignment.Multi_Vendor.Food.Delivery.converter;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.DishesResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Dishes;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import org.springframework.stereotype.Service;

@Service
public class PojoToDtoConverter {

    public DishesResponseDto convertDishToDishResponseDto(Dishes dishes){
        return new DishesResponseDto(
                dishes.getRestaurant().getRestaurantName(),
                dishes.getName(),
                dishes.getDescription(),
                dishes.getCuisine(),
                dishes.getPrice(),
                dishes.getRating()
        );
    }

    public OrderResponseDto convertOrderToOrderResponseDto(Orders placedOrder){
        return OrderResponseDto.builder()
                .customerEmail(placedOrder.getCustomers().getEmail())
                .customerName(placedOrder.getCustomers().getFirstName())
                .dishName(placedOrder.getDishName())
                .mode(placedOrder.getMode())
                .status(placedOrder.getStatus())
                .orderedAt(placedOrder.getOrderedAt())
                .orderId(placedOrder.getOrderId())
                .address(placedOrder.getCustomers().getAddress())
                .restaurantName(placedOrder.getRestaurantName())
                .price(placedOrder.getPrice())
                .build();
    }
}

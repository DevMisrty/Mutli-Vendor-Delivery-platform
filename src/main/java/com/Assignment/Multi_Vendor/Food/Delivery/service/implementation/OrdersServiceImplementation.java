package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.converter.PojoToDtoConverter;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.RestaurantResponseDTO;
import com.Assignment.Multi_Vendor.Food.Delivery.model.*;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DishesRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.OrdersRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.RestaurantRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.OrdersService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersServiceImplementation implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final DishesRepository dishesRepository;
    private final RestaurantRepository restaurantRepository;
    private final PojoToDtoConverter converter;
    private final CustomerRepository customerRepository;


    // Creates the order Entity from the dishName, and the Restaurant Id,
    // pending - currently hardcoded the customer Id, but after implementing spring Security, get the
    //           customer Id from the JWT token and fetch it from the database.
    @Override
    public OrderResponseDto placeOrder(String restName, String dishName) {

        Customers customers = customerRepository.findById(1L).orElseThrow();

        Dishes dish = dishesRepository.findByNameAndRestaurant_RestaurantName(dishName,restName).orElseThrow();
        Restaurant restaurant = restaurantRepository.findByRestaurantName(restName).orElseThrow();

        log.info("Restaurant :{}", restaurant);

        Integer quantity = 1;

        Orders order = Orders.builder()
                        .dishName(dish.getName())
                        .customers(customers)
                        .orderedAt(LocalDateTime.now())
                        .restaurantName(restaurant.getRestaurantName())
                        .mode(PaymentMode.CASH_ON_DELIVERY)
                        .status(OrderStatus.PLACED)
                        .restaurantName(restaurant.getRestaurantName())
                        .price(dish.getPrice() * quantity )
                        .build();

        Orders placedOrder = ordersRepository.save(order);

        log.info("OrderDetails :{}", order.getRestaurantName());

        return converter.convertOrderToOrderResponseDto(placedOrder);
    }


    // fetches the order details, based on Order id.
    // pending, after implementing spring security, before returning the OrderResponseDto, verify that
    //          the customer id from jwt token is matching with Order ENTITY fetched, if matched
    //             return OrderResponseDto, else return null.
    @Override
    public OrderResponseDto viewOrderDetails(Long orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow();
        return converter.convertOrderToOrderResponseDto(order);
    }


    // changes the status of the order,
    // pending -> after implementing spring security, verify that the restId from Jwt token matches
    //              with the one present inside the order entity.
    @Override
    public OrderResponseDto changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Orders order = ordersRepository.findById(orderId).orElseThrow();
        if(order.getStatus()==OrderStatus.DELIVERED){
            return converter.convertOrderToOrderResponseDto(order);
        }
        order.setStatus(orderStatus);
        Orders savedOrder = ordersRepository.save(order);
        return converter.convertOrderToOrderResponseDto(savedOrder);

    }
}

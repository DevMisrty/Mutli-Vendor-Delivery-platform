package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.CustomerAccessDeniedException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.DishNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.NoSuchOrderException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantAccessDeniedException;
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
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    @Override
    public OrderResponseDto placeOrder(String restName, String dishName, Customers customers) throws DishNotFoundException {

        Dishes dish = dishesRepository.findByNameAndRestaurant_RestaurantName(dishName,restName)
                .orElseThrow(
                        () -> new DishNotFoundException("No such Exception found")
                );
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
    @Override
    public OrderResponseDto viewOrderDetails(Long orderId, Long customerId) throws CustomerAccessDeniedException {
        Orders order = ordersRepository.findById(orderId).orElseThrow();
        if(order.getCustomers().getId()!= customerId) {
            throw new CustomerAccessDeniedException("");
        }
        return converter.convertOrderToOrderResponseDto(order);
    }


    // changes the status of the order,
    @Override
    public OrderResponseDto changeOrderStatus(Long orderId, OrderStatus orderStatus, String restName)
            throws NoSuchOrderException, RestaurantAccessDeniedException {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(()-> new NoSuchOrderException("No order with such id" + orderId));

        if(!order.getRestaurantName().equals(restName)){
            throw new RestaurantAccessDeniedException("You dont have access to change the order details");
        }

        if(order.getStatus()==OrderStatus.DELIVERED){
            return converter.convertOrderToOrderResponseDto(order);
        }
        order.setStatus(orderStatus);
        Orders savedOrder = ordersRepository.save(order);
        return converter.convertOrderToOrderResponseDto(savedOrder);

    }

    @Override
    public List<Orders> getAllOutForDeliveryOrders(Integer limit) {
        return ordersRepository.findAllByStatus(OrderStatus.OUT_FOR_DELIVERY, Limit.of(limit));
    }
}

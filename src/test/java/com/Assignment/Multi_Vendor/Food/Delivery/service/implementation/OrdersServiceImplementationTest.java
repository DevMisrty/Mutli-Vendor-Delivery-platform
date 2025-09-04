package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.*;
import com.Assignment.Multi_Vendor.Food.Delivery.converter.PojoToDtoConverter;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.*;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DishesRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.OrdersRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.RestaurantRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.OS;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Limit;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrdersServiceImplementationTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private DishesRepository dishesRepository;

    @Mock
    private PojoToDtoConverter converter;

    @InjectMocks
    private OrdersServiceImplementation ordersService;

    private List<Dishes> dishes = new ArrayList<>();

    private Customers customers;

    Restaurant restaurant1;
    Restaurant restaurant2;
    Orders orders;


    @BeforeEach
    public void setUp(){

        restaurant1 = new Restaurant(1L,"rest1","owner1","root","owner1@rest1.com");
        restaurant2 =
                new Restaurant(2L,"rest2","owner2","root","owner2@rest2.com");
        restaurant2.setStatus(STATUS.APPROVED);
        dishes.add(Dishes.builder()
                .id(1L)
                .name("Margherita Pizza")
                .description("Classic pizza with mozzarella, basil, and tomato sauce")
                .cuisine(Cuisine.ITALIAN)
                .price(299.0)
                .rating(0f)
                .count(0L)
                .restaurant(restaurant2) // can set restaurant if needed
                .build());

        dishes.add(Dishes.builder()
                .id(2L)
                .name("Paneer Butter Masala")
                .description("Paneer cooked in a rich creamy tomato gravy")
                .cuisine(Cuisine.INDIAN)
                .price(249.0)
                .rating(4.7f)
                .count(300L)
                .restaurant(restaurant1)
                .build());

        dishes.add(Dishes.builder()
                .id(3L)
                .name("Chicken Biryani")
                .description("Fragrant basmati rice cooked with spiced chicken")
                .cuisine(Cuisine.INDIAN)
                .price(349.0)
                .rating(4.6f)
                .count(500L)
                .restaurant(restaurant2)
                .build());

        dishes.add(Dishes.builder()
                .id(4L)
                .name("Sushi Platter")
                .description("Assorted sushi with soy sauce and wasabi")
                .cuisine(Cuisine.JAPANESE)
                .price(599.0)
                .rating(4.8f)
                .count(220L)
                .restaurant(null)
                .build());

        dishes.add(Dishes.builder()
                .id(5L)
                .name("Veg Hakka Noodles")
                .description("Stir-fried noodles with veggies and sauces")
                .cuisine(Cuisine.CHINESE)
                .price(199.0)
                .rating(4.3f)
                .count(180L)
                .restaurant(restaurant1)
                .build());

        customers = new Customers();
        customers.setId(1);
        customers.setEmail("dev.mistry1@gmail.com");
        customers.setPassword("root1");
        customers.setAddress("Ghodasar");
        customers.setFirstName("Dev");
        customers.setLastName("Mistry");
        customers.setPhoneNumber("1212121212");

        orders = Orders.builder()
                .dishName(dishes.get(0).getName())
                .customers(customers)
                .orderedAt(LocalDateTime.now())
                .restaurantName(restaurant1.getRestaurantName())
                .mode(PaymentMode.CASH_ON_DELIVERY)
                .status(OrderStatus.PLACED)
                .price(dishes.get(0).getPrice()  )
                .build();

    }

    @Test
    public void place_Order_Test() throws RestaurantNotFoundException, DishNotFoundException {

        String dishName = "Margherita Pizza";
        String restName = "restaurant1";

        when(dishesRepository.findByNameAndRestaurant_RestaurantName(dishName,restName))
                .thenReturn(Optional.empty());
        assertThrows(DishNotFoundException.class, ()-> ordersService.placeOrder(dishName,restName,customers));

        when(dishesRepository.findByNameAndRestaurant_RestaurantName(dishName,restName))
                .thenReturn(Optional.of(dishes.getFirst()));
        when(restaurantRepository.findByRestaurantName(restName))
                .thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class, ()-> ordersService.placeOrder(restName,dishName,customers));


        OrderResponseDto responseDto = converter.convertOrderToOrderResponseDto(orders);
        when(dishesRepository.findByNameAndRestaurant_RestaurantName(dishName,restName))
                .thenReturn(Optional.of(dishes.getFirst()));
        when(restaurantRepository.findByRestaurantName(restName))
                .thenReturn(Optional.of(restaurant1));
        when(ordersRepository.save(any()))
                .thenReturn(responseDto);

        OrderResponseDto result = ordersService.placeOrder(restName, dishName, customers);
        assertEquals(responseDto,result);
    }

    @Test
    public void viewOrderDetails_test() throws NoSuchOrderException, CustomerAccessDeniedException {
        when(ordersRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchOrderException.class,
                () -> ordersService.viewOrderDetails(1l, 1l));


        when(ordersRepository.findById(anyLong()))
                .thenReturn(Optional.of(orders));
        assertThrows(CustomerAccessDeniedException.class,
                ()-> ordersService.viewOrderDetails(1l,12l));

        OrderResponseDto resultDto = converter.convertOrderToOrderResponseDto(orders);
        assertEquals(resultDto, ordersService.viewOrderDetails(1l,1l) );
    }

    @Test
    public void changeOrderStatus_test() throws NoSuchOrderException, RestaurantAccessDeniedException {
        when(ordersRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchOrderException.class,
                () -> ordersService.changeOrderStatus(1l, OrderStatus.CONFIRMED, restaurant1.getRestaurantName()));

        when(ordersRepository.findById(anyLong()))
                .thenReturn(Optional.of(orders));
        assertThrows(RestaurantAccessDeniedException.class, ()-> ordersService.changeOrderStatus(1l,OrderStatus.CONFIRMED,restaurant2.getRestaurantName()));

        when(ordersRepository.save(any(Orders.class)))
                .thenReturn(orders);
        OrderResponseDto responseDto = converter.convertOrderToOrderResponseDto(orders);
        assertEquals(responseDto, ordersService.changeOrderStatus(1l,OrderStatus.PLACED,restaurant1.getRestaurantName()));

        orders.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        OrderResponseDto responseDto1 = converter.convertOrderToOrderResponseDto(orders);
        when(ordersRepository.save(orders))
                .thenReturn(orders);
        assertEquals(responseDto1,ordersService.changeOrderStatus(1l,OrderStatus.OUT_FOR_DELIVERY,restaurant1.getRestaurantName()));

        orders.setStatus(OrderStatus.DELIVERED);
        OrderResponseDto responseDto2 = converter.convertOrderToOrderResponseDto(orders);
        when(ordersRepository.save(orders))
                .thenReturn(orders);
        assertEquals(responseDto2,ordersService.changeOrderStatus(1l,OrderStatus.DELIVERED,restaurant1.getRestaurantName()));
    }

    @Test
    public void getAllOutForDeliveryOrders_test(){

        when(ordersRepository.findAllByStatus(OrderStatus.OUT_FOR_DELIVERY, Limit.of(1)))
                .thenReturn(List.of(orders));
        assertIterableEquals(List.of(orders),ordersService.getAllOutForDeliveryOrders(1));
    }
}
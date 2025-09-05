package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.DishNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.IncorrectInputException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.RestaurantNotFoundException;
import com.Assignment.Multi_Vendor.Food.Delivery.model.*;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DishesRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RestaurantServiceImplementationTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private DishesRepository dishesRepository;

    @InjectMocks
    private RestaurantServiceImplementation restaurantService;

    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Dishes> dishes = new ArrayList<>();

    @BeforeEach
    public void setup(){
        restaurants.add(Restaurant.builder()
                .id(1L)
                .restaurantName("Spicy Villa")
                .ownerName("Ramesh Patel")
                .email("spicyvilla@gmail.com")
                .password("encodedPass1")
                .ratings(4.5f)
                .count(120L)
                .role(ROLE.RESTAURANT_OWNER)
                .status(STATUS.APPROVED)
                .build());

        restaurants.add(Restaurant.builder()
                .id(2L)
                .restaurantName("Urban Tandoor")
                .ownerName("Meena Shah")
                .email("urbantandoor@gmail.com")
                .password("encodedPass2")
                .ratings(4.2f)
                .count(95L)
                .role(ROLE.RESTAURANT_OWNER)
                .status(STATUS.APPROVED)
                .build());

        restaurants.add(Restaurant.builder()
                .id(12L)
                .restaurantName("Urban hub")
                .ownerName("Meena Tripathi")
                .email("UrbanHub@gmail.com")
                .password("encodedPass2")
                .ratings(4.2f)
                .count(95L)
                .role(ROLE.RESTAURANT_OWNER)
                .status(STATUS.APPROVED)
                .build());

        restaurants.add(Restaurant.builder()
                .id(3L)
                .restaurantName("Royal Biryani House")
                .ownerName("Amit Kumar")
                .email("royalbiryani@gmail.com")
                .password("encodedPass3")
                .ratings(3.8f)
                .count(40L)
                .role(ROLE.RESTAURANT_OWNER)
                .status(STATUS.NOT_APPROVED)
                .build());

        restaurants.add(Restaurant.builder()
                .id(4L)
                .restaurantName("Foodie Junction")
                .ownerName("Priya Mehta")
                .email("foodiejunction@gmail.com")
                .password("encodedPass4")
                .ratings(4.7f)
                .count(230L)
                .role(ROLE.RESTAURANT_OWNER)
                .status(STATUS.APPROVED)
                .build());

        restaurants.add(Restaurant.builder()
                .id(5L)
                .restaurantName("Curry Culture")
                .ownerName("Ankit Verma")
                .email("curryculture@gmail.com")
                .password("encodedPass5")
                .ratings(3.5f)
                .count(25L)
                .role(ROLE.RESTAURANT_OWNER)
                .status(STATUS.NOT_APPROVED)
                .build());

        // Dishes for Restaurant 1: Spicy Villa (Indian)
        dishes.add(Dishes.builder()
                .id(1L)
                .name("Paneer Butter Masala")
                .description("Soft paneer cooked in rich buttery gravy")
                .cuisine(Cuisine.INDIAN)
                .price(250.0)
                .rating(4.5f)
                .count(150L)
                .restaurant(restaurants.get(0))
                .build());

        dishes.add(Dishes.builder()
                .id(2L)
                .name("Dal Tadka")
                .description("Yellow lentils tempered with spices")
                .cuisine(Cuisine.INDIAN)
                .price(180.0)
                .rating(4.3f)
                .count(90L)
                .restaurant(restaurants.get(0))
                .build());

        // Dishes for Restaurant 2: Urban Tandoor (Indian + Chinese)
        dishes.add(Dishes.builder()
                .id(3L)
                .name("Chicken Tandoori")
                .description("Marinated chicken roasted in a tandoor")
                .cuisine(Cuisine.INDIAN)
                .price(350.0)
                .rating(4.7f)
                .count(200L)
                .restaurant(restaurants.get(1))
                .build());

        dishes.add(Dishes.builder()
                .id(4L)
                .name("Veg Hakka Noodles")
                .description("Stir-fried noodles with fresh vegetables")
                .cuisine(Cuisine.CHINESE)
                .price(220.0)
                .rating(4.2f)
                .count(110L)
                .restaurant(restaurants.get(1))
                .build());

        // Dishes for Restaurant 3: Royal Biryani House (Indian)
        dishes.add(Dishes.builder()
                .id(5L)
                .name("Hyderabadi Biryani")
                .description("Aromatic biryani cooked with spices and herbs")
                .cuisine(Cuisine.INDIAN)
                .price(300.0)
                .rating(4.6f)
                .count(180L)
                .restaurant(restaurants.get(2))
                .build());

        dishes.add(Dishes.builder()
                .id(6L)
                .name("Mutton Korma")
                .description("Slow-cooked mutton in creamy curry")
                .cuisine(Cuisine.INDIAN)
                .price(450.0)
                .rating(4.8f)
                .count(140L)
                .restaurant(restaurants.get(2))
                .build());

        // Dishes for Restaurant 4: Foodie Junction (Italian + Indian)
        dishes.add(Dishes.builder()
                .id(7L)
                .name("Margherita Pizza")
                .description("Classic cheese pizza with fresh tomato sauce")
                .cuisine(Cuisine.ITALIAN)
                .price(400.0)
                .rating(4.5f)
                .count(170L)
                .restaurant(restaurants.get(3))
                .build());

        dishes.add(Dishes.builder()
                .id(8L)
                .name("Pasta Alfredo")
                .description("Creamy white sauce pasta topped with cheese")
                .cuisine(Cuisine.ITALIAN)
                .price(380.0)
                .rating(4.4f)
                .count(120L)
                .restaurant(restaurants.get(3))
                .build());

        // Dishes for Restaurant 5: Curry Culture (Mexican + Indian)
        dishes.add(Dishes.builder()
                .id(9L)
                .name("Chicken Quesadilla")
                .description("Mexican-style tortilla stuffed with chicken & cheese")
                .cuisine(Cuisine.MEXICAN)
                .price(420.0)
                .rating(4.3f)
                .count(130L)
                .restaurant(restaurants.get(4))
                .build());

        dishes.add(Dishes.builder()
                .id(10L)
                .name("Butter Chicken")
                .description("Tender chicken cooked in buttery tomato gravy")
                .cuisine(Cuisine.INDIAN)
                .price(320.0)
                .rating(4.7f)
                .count(200L)
                .restaurant(restaurants.get(4))
                .build());
    }

    @Test
    public void getAllNotApprovedRestaurant_test(){
        List<Restaurant> notApprovedRestaurant = restaurants.stream()
                .filter(rest -> rest.getStatus().equals(STATUS.APPROVED))
                .collect(Collectors.toList());
        when(restaurantRepository.findAllByStatus(STATUS.NOT_APPROVED))
                .thenReturn(notApprovedRestaurant);

        assertIterableEquals(notApprovedRestaurant, restaurantService.getAllNotApprovedRestaurant());

        when(restaurantRepository.findAllByStatus(STATUS.NOT_APPROVED))
                .thenReturn(null);
        assertNull(restaurantService.getAllNotApprovedRestaurant());
    }

    @Test
    public void getAllRestaurant_test(){
        when(restaurantRepository.findAll())
                .thenReturn(null);
        assertNull(restaurantService.getAllRestaurant());

        when(restaurantRepository.findAll())
                .thenReturn(restaurants);
        assertIterableEquals(restaurants,restaurantService.getAllRestaurant());
    }


    @Test
    public void approvedRestaurant_Test() throws RestaurantNotFoundException {
        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.approvedRestaurant(1l));

        Restaurant restaurant = restaurants.get(0);
        restaurant.setStatus(STATUS.APPROVED);
        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class)))
                .thenReturn(restaurant);
        assertEquals(restaurant,restaurantService.approvedRestaurant(1l));
        assertNotEquals(restaurants.get(2),restaurantService.approvedRestaurant(2l));
    }

    @Test
    public void addNewRestaurant_test() throws RestaurantNameAlreadyTakenException {
        Restaurant restaurant = restaurants.get(0);
        String restName = restaurant.getRestaurantName();
        when(restaurantRepository.existsByRestaurantName(anyString()))
                .thenReturn(true);

        assertThrows(RestaurantNameAlreadyTakenException.class,
                ()-> restaurantService.addNewRestaurant(restaurant));

        when(restaurantRepository.existsByRestaurantName(anyString()))
                .thenReturn(false);
        when(restaurantRepository.save(restaurant))
                .thenReturn(restaurant);
        assertDoesNotThrow(()->restaurantService.addNewRestaurant(restaurant));
        assertEquals(restaurant,restaurantService.addNewRestaurant(restaurant));
    }

    @Test
    public void addNewMenu_test() throws RestaurantNotFoundException {
        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.addNewMenu(dishes,1L));

        when(restaurantRepository.findById(12L))
                .thenReturn(Optional.of(restaurants.get(3)));
        restaurants.get(3).setMenu(dishes);
        when(restaurantRepository.save(restaurants.get(3)))
                .thenReturn(restaurants.get(3));
        assertEquals(restaurants.get(3),restaurantService.addNewMenu(dishes,12L));

        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.of(restaurants.get(1)));
        restaurants.get(1).setMenu(new ArrayList<>());
        when(restaurantRepository.save(restaurants.get(1)))
                .thenReturn(restaurants.get(1));
        assertEquals(restaurants.get(1),restaurantService.addNewMenu(dishes,2L));
    }

    @Test
    public void addDishesToMenu_test() throws IncorrectInputException, RestaurantNotFoundException {
        Restaurant restaurant = restaurants.get(1);
        restaurant.setMenu(new ArrayList<Dishes>());

        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.addDishesToMenu(dishes,1L));

        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.of(restaurant));
        assertThrows(IncorrectInputException.class,
                ()-> restaurantService.addDishesToMenu(new ArrayList<>(), 1l));

        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant))
                .thenReturn(restaurant);
        assertEquals(restaurant,restaurantService.addDishesToMenu(dishes, 1L));
    }

    @Test
    public void removeDishFromMenu_test() throws RestaurantNotFoundException, DishNotFoundException {

        String dishName = dishes.getFirst().getName();
        String restName = dishes.getFirst().getRestaurant().getRestaurantName();

        when(dishesRepository.findByNameAndRestaurant_RestaurantName(anyString(),anyString()))
                .thenReturn(Optional.empty());
        assertThrows(DishNotFoundException.class, ()-> restaurantService.removeDishFromMenu(dishName,restName));

        when(dishesRepository.findByNameAndRestaurant_RestaurantName(anyString(),anyString()))
                .thenReturn(Optional.of(dishes.getFirst()));
        when(restaurantRepository.findByRestaurantName(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class, ()-> restaurantService.removeDishFromMenu(dishName,restName));

        restaurants.getFirst().setMenu(new ArrayList<>(dishes));
        when(restaurantRepository.findByRestaurantName(anyString()))
                .thenReturn(Optional.of(restaurants.getFirst()));
        when(restaurantRepository.save(restaurants.getFirst()))
                .thenReturn(restaurants.getFirst());
        assertEquals(restaurants.getFirst(),
                restaurantService.removeDishFromMenu(dishName,restName));
    }

    @Test
    public void disApproveTheRestaurant_test() throws RestaurantNotFoundException {

        Restaurant restaurant = restaurants.getFirst();

        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.disApproveTheRestaurant(restaurant.getId()));

        restaurant.setStatus(STATUS.NOT_APPROVED);
        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.of(restaurant));
        assertEquals(restaurant,restaurantService.disApproveTheRestaurant(restaurant.getId()));

        restaurant.setStatus(STATUS.APPROVED);
        when(restaurantRepository.findById(anyLong()))
                .thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant))
                .thenReturn(restaurant);
        assertEquals(STATUS.NOT_APPROVED, restaurantService.disApproveTheRestaurant(restaurant.getId()).getStatus());
    }

    @Test
    public void getMenuByRestaurantName_test() throws RestaurantNotFoundException {

        Restaurant restaurant = restaurants.getFirst();
        restaurant.setMenu(new ArrayList<>(dishes));

        when(restaurantRepository.findByRestaurantName(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.getMenuByRestaurantName(restaurant.getRestaurantName()));

        when(restaurantRepository.findByRestaurantName(anyString()))
                .thenReturn(Optional.of(restaurant));
        assertIterableEquals(dishes, restaurantService.getMenuByRestaurantName(restaurant.getRestaurantName()));
    }

    @Test
    public void getRestaurantByName() throws RestaurantNotFoundException {
        Restaurant restaurant = restaurants.getFirst();

        when(restaurantRepository.findByRestaurantName(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.getRestaurantByName(restaurant.getRestaurantName()));

        when(restaurantRepository.findByRestaurantName(anyString()))
                .thenReturn(Optional.of(restaurant));
        assertEquals(restaurant,restaurantService.getRestaurantByName(restaurant.getRestaurantName()));
    }

    @Test
    public void getRestaurantByEmail() throws RestaurantNotFoundException {
        Restaurant restaurant = restaurants.getFirst();

        when(restaurantRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.getRestaurantByEmail(restaurant.getEmail()));

        when(restaurantRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(restaurant));
        assertEquals(restaurant,restaurantService.getRestaurantByEmail(restaurant.getEmail()));
    }

    @Test
    public void checkIfRestaurantExists_test() throws RestaurantNotFoundException {
        Restaurant restaurant = restaurants.getFirst();

        when(restaurantRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,
                ()-> restaurantService.checkIfRestaurantExists(restaurant.getEmail()));

        when(restaurantRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(restaurant));
        assertEquals(restaurant, restaurantService.checkIfRestaurantExists(restaurant.getEmail()));
    }


    @Test
    public void checkIfRestaurantExistsAndApproved(){
        Restaurant restaurant = restaurants.getFirst();

        when(restaurantRepository.findByRestaurantNameAndStatus(anyString(),any()))
                .thenReturn(restaurant);
        assertEquals(restaurant,
                restaurantService.checkIfRestaurantExistsAndApproved(restaurant.getRestaurantName()));
    }
}
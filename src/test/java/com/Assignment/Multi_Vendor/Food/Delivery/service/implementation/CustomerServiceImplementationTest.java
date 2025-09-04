package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.UserNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.CustomerDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerServiceImplementationTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImplementation customerService;

    private Customers customers;
    private Customers customer1;

    @BeforeEach
    public void setupDependency(){
        customers = new Customers();
        customers.setId(1);
        customers.setEmail("dev.mistry1@gmail.com");
        customers.setPassword("root1");
        customers.setAddress("Ghodasar");
        customers.setFirstName("Dev");
        customers.setLastName("Mistry");
        customers.setPhoneNumber("1212121212");

    }

    @Test
    public void addNewCustomer_Test() throws UserNameAlreadyTakenException {
        when(customerRepository.existsCustomersByEmail(customers.getEmail()))
                .thenReturn(false);

        when(customerRepository.save(customers))
                .thenReturn(customers);

        Customers savedCustomer = customerService.addNewCustomer(customers);

        assertNotNull(savedCustomer);
        assertEquals(customers.getEmail(), savedCustomer.getEmail());

        when(customerRepository.existsCustomersByEmail(customers.getEmail()))
                .thenReturn(true);
        assertThrows(UserNameAlreadyTakenException.class,()-> customerService.addNewCustomer(customers));
    }
}
package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.GlobalExceptionHandler.ExceptionClasses.UserNameAlreadyTakenException;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImplementation implements CustomerService {


    private final CustomerRepository customerRepository;

    @Override
    public Customers addNewCustomer(Customers customer) throws UserNameAlreadyTakenException {
        if(customerRepository.existsCustomersByEmail(customer.getEmail())){
            throw new UserNameAlreadyTakenException();
        }
       return customerRepository.save(customer);
    }
}

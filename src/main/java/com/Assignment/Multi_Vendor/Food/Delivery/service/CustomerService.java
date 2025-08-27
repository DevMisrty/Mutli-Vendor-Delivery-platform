package com.Assignment.Multi_Vendor.Food.Delivery.service;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;

public interface CustomerService {
    Customers addNewCustomer(Customers customer);
}

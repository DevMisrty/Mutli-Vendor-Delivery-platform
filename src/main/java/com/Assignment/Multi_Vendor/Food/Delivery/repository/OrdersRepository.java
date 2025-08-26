package com.Assignment.Multi_Vendor.Food.Delivery.repository;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends ListCrudRepository<Orders,Long> {
}

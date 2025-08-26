package com.Assignment.Multi_Vendor.Food.Delivery.repository;

import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends ListCrudRepository<Orders,Long> {

    List<Orders> findAllByStatus(OrderStatus status, Limit limit);

    OrderStatus Status(OrderStatus status);
}

package com.Assignment.Multi_Vendor.Food.Delivery.repository;

import com.Assignment.Multi_Vendor.Food.Delivery.model.DeliveryAgent;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeliveryAgentRepository extends ListCrudRepository<DeliveryAgent, Long> {

    List<DeliveryAgent> findByAvaibiltyBefore(Date avaibiltyBefore);
}

package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.OrderResponseDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.DeliveryAgent;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DeliveryAgentRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.OrdersRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DeliveryAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAgentServiceImplementation implements DeliveryAgentService {


    private final DeliveryAgentRepository deliveryAgentRepository;


    @Override
    public List<DeliveryAgent> getAllDeliveryAgents() {
        return deliveryAgentRepository.findAll();
    }
}


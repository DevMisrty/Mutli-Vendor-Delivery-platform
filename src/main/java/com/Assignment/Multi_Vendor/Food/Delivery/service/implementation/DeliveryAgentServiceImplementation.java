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
    private final OrdersRepository ordersRepository;


    @Override
    public Orders assignDeliveryAgent(Long orderId) {

        List<DeliveryAgent> agents = deliveryAgentRepository.findAll();

        if(agents.isEmpty()){
            return new Orders();
        }

        Orders order = ordersRepository.findById(orderId).orElseThrow();

        for(DeliveryAgent agent: agents){
            if(agent.getAvaibilty().before(new Date())){
                order.setAgent(agent);
                agent.setAvaibilty(new Date(System.currentTimeMillis() + 1000 * 60 * 20));
                ordersRepository.save(order);
                deliveryAgentRepository.save(agent);
                break;
            }
        }

        return order;
    }

    @Override
    public List<DeliveryAgent> getAllDeliveryAgents() {
        return deliveryAgentRepository.findAll();
    }
}


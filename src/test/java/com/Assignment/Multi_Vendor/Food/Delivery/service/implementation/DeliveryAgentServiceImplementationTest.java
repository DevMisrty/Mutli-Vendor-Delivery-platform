package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.model.DeliveryAgent;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DeliveryAgentRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.OrdersRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DeliveryAgentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class DeliveryAgentServiceImplementationTest {

    @Mock
    private DeliveryAgentRepository deliveryAgentRepository;

    @InjectMocks
    private DeliveryAgentServiceImplementation agentService;

    private List<DeliveryAgent> agents= new ArrayList<>();

    @BeforeEach
    public void setup(){
        agents.add(DeliveryAgent.builder()
                .id(1L)
                .firstName("Rahul")
                .lastName("Sharma")
                .avaibilty(new Date())
                .phoneNumber("9876543210")
                .build());

        agents.add(DeliveryAgent.builder()
                .id(2L)
                .firstName("Priya")
                .lastName("Mehta")
                .avaibilty(new Date())
                .phoneNumber("9988776655")
                .build());

        agents.add(DeliveryAgent.builder()
                .id(3L)
                .firstName("Arjun")
                .lastName("Patel")
                .avaibilty(new Date())
                .phoneNumber("9123456780")
                .build());

        agents.add(DeliveryAgent.builder()
                .id(4L)
                .firstName("Simran")
                .lastName("Kaur")
                .avaibilty(new Date())
                .phoneNumber("9001122334")
                .build());

        agents.add(DeliveryAgent.builder()
                .id(5L)
                .firstName("Amit")
                .lastName("Rao")
                .avaibilty(new Date())
                .phoneNumber("9990001112")
                .build());
    }

    @Test
    public void getAllDeliveryAgents_test(){
        when(deliveryAgentRepository.findAll())
                .thenReturn(null);

        assertNull(agentService.getAllDeliveryAgents());

        when(deliveryAgentRepository.findAll())
                .thenReturn(agents);

        assertIterableEquals(agents,agentService.getAllDeliveryAgents());
    }

    @Test
    public void findByRestaurantNameAndStatus_test(){
        when(deliveryAgentRepository.findByAvaibiltyBefore(any()))
                .thenReturn(null);
        assertNull(agentService.getAllDeliveryAgents(new Date()));

        when(deliveryAgentRepository.findByAvaibiltyBefore(any()))
                .thenReturn(agents);
        assertIterableEquals(agents,agentService.getAllDeliveryAgents(new Date()));
    }
}
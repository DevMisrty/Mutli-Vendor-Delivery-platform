package com.Assignment.Multi_Vendor.Food.Delivery.schedular;

import com.Assignment.Multi_Vendor.Food.Delivery.model.DeliveryAgent;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DeliveryAgentRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.OrdersRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DeliveryAgentService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliverySchedular {


    private final DeliveryAgentService deliveryAgentService;
    private final OrdersService ordersService;
    private final OrdersRepository ordersRepository;
    private final DeliveryAgentRepository deliveryAgentRepository;


    @Scheduled( cron = "0 */5 * * * *")
    public void checkForDeliveryAgentAndAssignOrders(){
        log.info("Schedular started");
        List<DeliveryAgent> agents = deliveryAgentService.getAllDeliveryAgents()
                .stream()
                .filter(agent ->
                        agent.getAvaibilty().before(new Date(System.currentTimeMillis())))
                .collect(Collectors.toList());

        List<Orders> outForDeliveryOrders = ordersService.getAllOutForDeliveryOrders(agents.size());

        if(agents.isEmpty() || outForDeliveryOrders.isEmpty() ){
            log.info("No Delivery Agent is Available, Or no Order to deliver");
            return;
        }

        int i=0;

        for(Orders order : outForDeliveryOrders){
            DeliveryAgent agent = agents.get(i);
            i++;

            order.setAgent(agent);
            agent.setOrders(order);

            ordersRepository.save(order);
            deliveryAgentRepository.save(agent);
        }
    }

}

package com.Assignment.Multi_Vendor.Food.Delivery.schedular;

import com.Assignment.Multi_Vendor.Food.Delivery.model.DeliveryAgent;
import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;
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


    @Scheduled( cron = "0 * * * * *")
    public void checkForDeliveryAgentAndAssignOrders(){
        log.info("checkForDeliveryAgentAndAssignOrders, Schedular started");

        List<DeliveryAgent> allDeliveryAgents = deliveryAgentService.getAllDeliveryAgents();

        List<DeliveryAgent> agents = allDeliveryAgents
                .stream()
                .filter(agent ->
                        agent.getAvaibilty().before(new Date(System.currentTimeMillis())))
                .collect(Collectors.toList());



        if(agents.isEmpty() ){
            log.info("No Delivery Agent is Available ");
            return;
        }

        List<Orders> outForDeliveryOrders = ordersService.getAllOutForDeliveryOrders(agents.size());

        if(outForDeliveryOrders.isEmpty()){
            log.info("No Order to deliver");
        }
        int i=0;

        for(Orders order : outForDeliveryOrders){

            log.info("before checking agentid");
            if(order.getAgent()!=null){
                log.info(" agentid is not null");
                continue;
            }
            log.info("after checking agentid");
            DeliveryAgent agent = agents.get(i);
            i++;

            agent.setAvaibilty(new Date(System.currentTimeMillis() + 1000 * 60 * 2 ));
            order.setAgent(agent);
            agent.setOrders(order);

            ordersRepository.save(order);
            deliveryAgentRepository.save(agent);

            log.info("{}, order has been assigned to {} delivery agent. " , order.getOrderId(),agent.getFirstName() + agent.getLastName());
        }
    }

    @Scheduled( cron = "0 * * * * *")
    public void changeTheOrdersStatus(){
        log.info("changeTheOrdersStatus, schedular started");
        List<Orders> orders = ordersRepository.findAllByStatus(OrderStatus.OUT_FOR_DELIVERY)
                .stream()
                .filter(order -> order.getAgent()!=null)
                .filter(order -> order
                        .getAgent().getAvaibilty().before(new Date()))
                .collect(Collectors.toList());
        for (Orders order : orders){
            order.setStatus(OrderStatus.DELIVERED);
            order.setAgent(null);
            ordersRepository.save(order);

            log.info(order.getOrderId() + " " + order.getDishName()+" has been delivered. ");
        }
    }

}

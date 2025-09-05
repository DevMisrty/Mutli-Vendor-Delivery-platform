package com.Assignment.Multi_Vendor.Food.Delivery.schedular.quartzjobs;

import com.Assignment.Multi_Vendor.Food.Delivery.model.DeliveryAgent;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.DeliveryAgentRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.OrdersRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.DeliveryAgentService;
import com.Assignment.Multi_Vendor.Food.Delivery.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssignDeliveryAgentJob implements Job {

    private final DeliveryAgentService deliveryAgentService;
    private final OrdersService ordersService;
    private final OrdersRepository ordersRepository;
    private final DeliveryAgentRepository deliveryAgentRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("checkForDeliveryAgentAndAssignOrders, Schedular started");

         List<DeliveryAgent> agents =
                 deliveryAgentService.getAllDeliveryAgents(new Date(System.currentTimeMillis()));
//        List<DeliveryAgent> allDeliveryAgents = deliveryAgentService.getAllDeliveryAgents();
//        List<DeliveryAgent> agents = allDeliveryAgents
//                .stream()
//                .filter(agent ->
//                        agent.getAvaibilty().before(new Date(System.currentTimeMillis())))
//                .collect(Collectors.toList());
        if (agents.isEmpty()) {
            log.info("No Delivery Agent is Available ");
            return;
        }

        List<Orders> outForDeliveryOrders = ordersService.getAllOutForDeliveryOrders(agents.size());
        if (outForDeliveryOrders.isEmpty()) {
            log.info("No Order to deliver");
        }


        int i = 0;
        for (Orders order : outForDeliveryOrders) {
            if (order.getAgent() != null) {
                log.info(" agent Name  " + order.getAgent().getFirstName() +
                        order.getAgent().getLastName() + " is assigned to order " + order.getOrderId());
                continue;
            }
            DeliveryAgent agent = agents.get(i);
            i++;
            agent.setAvaibilty(new Date(System.currentTimeMillis() + 1000 * 60 * 2));
            order.setAgent(agent);
            agent.setOrders(order);
            ordersRepository.save(order);
            deliveryAgentRepository.save(agent);

            log.info("Order Id,{} has been assigned to  delivery agent, {} ", order.getOrderId(), agent.getFirstName() + agent.getLastName());

        }
    }
}

package com.Assignment.Multi_Vendor.Food.Delivery.schedular.quartzjobs;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.EmailDetailsDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.OrdersRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.service.implementation.OTPAuthService;
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
public class ChangeOrderStatusJob implements Job {

    private final OrdersRepository ordersRepository;
    private final OTPAuthService mailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("changeTheOrdersStatus, schedular started");
        List<Orders> orders = ordersRepository.findAllByStatus(OrderStatus.OUT_FOR_DELIVERY)
                .stream()
                .filter(order -> order.getAgent() != null)
                .filter(order -> order
                        .getAgent().getAvaibilty().before(new Date()))
                .collect(Collectors.toList());
        if (orders.isEmpty()) {
            log.info("No order is set to delivered today. ");
            return;
        }
        for (Orders order : orders) {

            EmailDetailsDto dto = EmailDetailsDto.builder()
                    .to(order.getCustomers().getEmail())
                    .build();

            mailService.mailSender(dto, order);
            order.setStatus(OrderStatus.DELIVERED);
            order.setAgent(null);
            ordersRepository.save(order);

            log.info(order.getOrderId() + " " + order.getDishName() + " has been delivered. ");
        }
    }
}

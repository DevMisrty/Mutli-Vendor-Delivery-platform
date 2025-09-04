package com.Assignment.Multi_Vendor.Food.Delivery.schedular;

import com.Assignment.Multi_Vendor.Food.Delivery.schedular.quartzjobs.AssignDeliveryAgentJob;
import com.Assignment.Multi_Vendor.Food.Delivery.schedular.quartzjobs.ChangeOrderStatusJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliverySchedularQuartz {

    // Change Status Job Builder
    @Bean
    public JobDetail statusChangeDetail(){
        return JobBuilder
                .newJob(ChangeOrderStatusJob.class)
                .withIdentity("Changing Status")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTriggerBuilder(){
        return TriggerBuilder
                .newTrigger()
                .forJob(statusChangeDetail())
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
                .withIdentity("Status Change Schedular")
                .build();
    }

    // Assign Delivery Job

    @Bean
    public JobDetail assignDeliveryAgent(){
        return JobBuilder
                .newJob(AssignDeliveryAgentJob.class)
                .withIdentity("Assign Delivery Agent to order")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger assignAgentToOrderBuilder(){
        return TriggerBuilder
                .newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
                .withIdentity("Assign Agent to Order Trigger")
                .forJob(assignDeliveryAgent())
                .build();
    }
}

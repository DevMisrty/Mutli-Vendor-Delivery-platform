package com.Assignment.Multi_Vendor.Food.Delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MultiVendorFoodDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiVendorFoodDeliveryApplication.class, args);
 	}

}

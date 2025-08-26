package com.Assignment.Multi_Vendor.Food.Delivery.dto;

import com.Assignment.Multi_Vendor.Food.Delivery.model.OrderStatus;
import com.Assignment.Multi_Vendor.Food.Delivery.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long orderId;
    private String dishName;
    private String restaurantName;
    private String address;
    private PaymentMode mode;
    private Double price;
    private LocalDateTime orderedAt;
    private OrderStatus status;

    private String deliveryAgentName;
    private Long deliveryAgentId;

    private String customerName;
    private String customerEmail;
}

package com.Assignment.Multi_Vendor.Food.Delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private String dishName;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    private PaymentMode mode = PaymentMode.CASH_ON_DELIVERY;

    private LocalDateTime orderedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PLACED;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cutomer_id")
    private Customers customers;

    @OneToOne(fetch = FetchType.EAGER)
    private DeliveryAgent agent;

    @Override
    public String toString() {
        return "Orders{" +
                "dishName='" + dishName + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", price=" + price +
                ", mode=" + mode +
                ", orderedAt=" + orderedAt +
                ", status=" + status +
                ", customers=" + customers +
                '}';
    }
}

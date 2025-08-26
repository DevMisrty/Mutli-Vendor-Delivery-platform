package com.Assignment.Multi_Vendor.Food.Delivery.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


public enum OrderStatus {

    PLACED,
    CONFIRMED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED
}
package com.Assignment.Multi_Vendor.Food.Delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class DeliveryAgent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    private Date avaibilty = new Date();

    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne(mappedBy = "agent")
    @JsonIgnore
    private Orders orders;
}

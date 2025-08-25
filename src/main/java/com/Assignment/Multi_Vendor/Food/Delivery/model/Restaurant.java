package com.Assignment.Multi_Vendor.Food.Delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
public class Restaurant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String restaurantName;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "restaurant_menu", joinColumns = @JoinColumn(name = "restaurant_id"))
    private List<Dishes> menu;

    private Integer reviews;

    private ROLE role = ROLE.RESTAURANT_OWNER;

    private STATUS status = STATUS.NOT_APPROVED;
}


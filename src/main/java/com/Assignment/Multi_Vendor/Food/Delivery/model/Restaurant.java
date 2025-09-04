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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "restaurant",orphanRemoval = true)
    private List<Dishes> menu;

    private Float ratings = 0f;
    private Long count = 0L ;

    @Enumerated(EnumType.STRING)
    private ROLE role = ROLE.RESTAURANT_OWNER;

    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.NOT_APPROVED;

    @Override
    public String toString() {
        return "Restaurant{" +
                "role=" + role +
                ", status=" + status +
                ", count=" + count +
                ", ratings=" + ratings +
                ", email='" + email + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                '}';
    }

    public Restaurant(Long id, String restaurantName, String ownerName, String password, String email) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.ownerName = ownerName;
        this.password = password;
        this.email = email;
    }
}


package com.Assignment.Multi_Vendor.Food.Delivery.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Data
@Entity
public class Dishes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;

    private Double price;

    private Float rating = 0f;

    private Long count = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore  // Prevent infinite recursion
    private Restaurant restaurant;


    @Override
    public String toString() {
        return "Dishes{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cuisine=" + cuisine +
                ", price=" + price +
                ", rating=" + rating +
                ", count=" + count +
                '}';
    }
}

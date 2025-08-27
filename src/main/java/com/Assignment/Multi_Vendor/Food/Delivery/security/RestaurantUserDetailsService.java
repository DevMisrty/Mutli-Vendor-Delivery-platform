package com.Assignment.Multi_Vendor.Food.Delivery.security;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Restaurant;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantUserDetailsService implements UserDetailsService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Restaurant> restaurant = restaurantRepository.findByEmail(email);
        if(restaurant.isPresent()){
            return new User(
                    restaurant.get().getEmail(),
                    restaurant.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + restaurant.get().getRole()))
            );
        }
        throw new UsernameNotFoundException("No such username found");
    }
}

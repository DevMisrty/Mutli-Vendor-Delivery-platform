package com.Assignment.Multi_Vendor.Food.Delivery.security;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Customers;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.AdminRepository;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.CustomerRepository;
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
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customers> customers = customerRepository.findByEmail(email);
        if(customers.isPresent()){
            return new User(
                    customers.get().getEmail(),
                    customers.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + customers.get().getRole()))
            );
        }
        throw new UsernameNotFoundException("No such username found");
    }
}

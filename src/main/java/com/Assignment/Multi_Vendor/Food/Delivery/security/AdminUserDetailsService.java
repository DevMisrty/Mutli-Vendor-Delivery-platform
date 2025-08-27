package com.Assignment.Multi_Vendor.Food.Delivery.security;

import com.Assignment.Multi_Vendor.Food.Delivery.model.Admin;
import com.Assignment.Multi_Vendor.Food.Delivery.repository.AdminRepository;
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
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if(admin.isPresent()){
            return new User(
                    admin.get().getEmail(),
                    admin.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + admin.get().getRole()))
            );
        }
        throw new UsernameNotFoundException("No such username found");
    }
}

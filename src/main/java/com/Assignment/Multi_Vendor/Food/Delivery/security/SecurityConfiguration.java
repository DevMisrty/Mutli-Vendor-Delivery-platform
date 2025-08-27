package com.Assignment.Multi_Vendor.Food.Delivery.security;

import com.Assignment.Multi_Vendor.Food.Delivery.JWt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomerUserDetailsService customerService;
    private final RestaurantUserDetailsService ownerService;
    private final AdminUserDetailsService adminService;
    private final PasswordEncoder passwordEncoder;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( auth ->
                        auth
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/order/**",
                                        "/feedback/**").hasRole("CUSTOMER")
                                .requestMatchers("/rest/**").hasRole("RESTAURANT_OWNER")
                                .requestMatchers("/auth/**","/menu/**").permitAll()

                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider customerProvider = new DaoAuthenticationProvider();
        customerProvider.setUserDetailsService(customerService);
        customerProvider.setPasswordEncoder(passwordEncoder);

        DaoAuthenticationProvider adminProvider = new DaoAuthenticationProvider();
        adminProvider.setUserDetailsService(adminService);
        adminProvider.setPasswordEncoder(passwordEncoder);

        DaoAuthenticationProvider ownerProvider = new DaoAuthenticationProvider();
        ownerProvider.setUserDetailsService(ownerService);
        ownerProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(List.of(customerProvider, adminProvider, ownerProvider));
    }
}

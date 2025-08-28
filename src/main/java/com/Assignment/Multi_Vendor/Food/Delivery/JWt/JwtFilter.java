package com.Assignment.Multi_Vendor.Food.Delivery.JWt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token ;
        String email;
        String role;

        String authToken = request.getHeader("Authorization");
        if(authToken==null || !authToken.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        token = authToken.substring(7);

        if(jwtUtility.isTokenValid(token)){
            email = jwtUtility.getEmailFromToken(token);
            role = jwtUtility.getRoleFromToken(token);

            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_"+role))
                            )
                    );
        }

        filterChain.doFilter(request,response);
        return;
    }
}

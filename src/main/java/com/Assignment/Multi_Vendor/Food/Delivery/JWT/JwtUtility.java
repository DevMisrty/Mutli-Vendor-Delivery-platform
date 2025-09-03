package com.Assignment.Multi_Vendor.Food.Delivery.JWT;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.Users;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtUtility {

    private String key ="jakhjhdkjhfjahdlhfjahldjfahfdldhfkjbdfhlbadfljglaueiwbfljbcoygahjebfcuyabcam;lsc;aguicbj";

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Users users){
        return Jwts.builder()
                .subject(users.getEmail())
                .claim("ROLE",users.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 24 ))
                .signWith(getSecretKey())
                .compact();
    }

    public Boolean isTokenValid(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .after(new Date());
    }

    public String getEmailFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("ROLE", String.class);
    }
}

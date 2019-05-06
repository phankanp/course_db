package com.school.schooldb.security;

import com.school.schooldb.model.User;
import com.school.schooldb.service.UserAdapter;
import com.school.schooldb.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@PropertySource("classpath:auth.properties")
public class JwtProvider {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        // Retrieves Spring security principal and casts to Useradapter instance
        UserAdapter userAdapter = (UserAdapter) authentication.getPrincipal();

        // Creating Date object and creating expiration date object
        Date now = new Date();
        Date expirationDate = new Date((now.getTime() + jwtExpirationInMs));

        // Creating the JWT
        return Jwts.builder()
                .setSubject(userAdapter.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // Returns username from JWT token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    // Checks digital signature of JWT to validate and verify authenticity
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }
}

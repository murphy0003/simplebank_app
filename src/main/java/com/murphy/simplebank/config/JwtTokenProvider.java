package com.murphy.simplebank.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Getter
public class JwtTokenProvider {
    private final SecretKey key;
    private final long expirationMs;
    public JwtTokenProvider( @Value("${jwt.secret:#{null}}") String secret,@Value("${jwt.expiration.ms:3600000}") long expirationMs){

        this.expirationMs = expirationMs;
        this.key = (secret!=null)? Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)):generateSecureKey();
    }

    private SecretKey generateSecureKey() {
        return Jwts.SIG.HS256.key().build();
    }
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime()+expirationMs);
        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(getKey())
                .compact();
    }
    public String getUsername(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();

    }
    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

}

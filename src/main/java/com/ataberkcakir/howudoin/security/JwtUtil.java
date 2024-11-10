package com.ataberkcakir.howudoin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private Claims extractAllClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> additionalClaims) {
        Map<String, Object> claims = new HashMap<>();
        claims.putAll(additionalClaims);
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirayDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirayDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

package com.example.gestion_incident.config;

import com.example.gestion_incident.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "96b0f86c8c9845c53c5783132ccf1f26a07f2b7d3a2ed37b89deabb5f17fb807";

    // ==========================
    // Extract username (email)
    // ==========================
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ==========================
    // Extract user ID
    // ==========================
    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("id", Integer.class));
    }

    // ==========================
    // Extract any claim
    // ==========================
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ==========================
    // Generate JWT with user info
    // ==========================
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof User user) {
            claims.put("id", user.getId()); // <-- ADDED THIS LINE
            claims.put("role", user.getRole().name());
            claims.put("firstname", user.getFirstname());
            claims.put("lastname", user.getLastname());
            claims.put("email", user.getEmail());
        }

        return generateToken(claims, userDetails);
    }

    // ==========================
    // Generate JWT
    // ==========================
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // email
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24)) // 24h
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ==========================
    // Validate token
    // ==========================
    public boolean isTokenValid(String token, UserDetails userDetails) {

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // ==========================
    // Check expiration
    // ==========================
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ==========================
    // Extract expiration
    // ==========================
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ==========================
    // Extract all claims
    // ==========================
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ==========================
    // Secret key
    // ==========================
    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
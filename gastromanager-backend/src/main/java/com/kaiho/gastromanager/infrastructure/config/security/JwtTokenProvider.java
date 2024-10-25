package com.kaiho.gastromanager.infrastructure.config.security;

import com.kaiho.gastromanager.domain.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    // TODO: Move this to application.properties
    private String SECRET_KEY = "41592ff6db6083e3f308bb1f18e5b3ea953e488729c7c2b13eb5a825388c6e0ff32af8d68fc28611f0f39f00df50827a608377b785d3a0a11361d821bf07f421a545c6e859c42c20611cee8331f06a699acc70d7ea4cd88dadb98caaaf4db6345111f78a1ec6f790f5507dcca16cbb4a4f21648365615f8399abc7d3d39f45901f6612c9d8b6ecc2910aa51cd63b07696b11e4dc90f892218725c3bfd25a20b0a8d5aa0bda396fe78c15a155196d00d7e61b9ae91d1aea51e790dfd8ee1b7bfae653248703a9e7ff56e70d97f9f8c889bb1b6cab9a9993458a8a550cfbf8c68e4cd74b28509d46546c6b530c1a1850f21b9b922fda902dc9b474dd930f741ad2";
    /*
        @Value("${jwt.expiration}")*/
    private long EXPIRATION_TIME = 86400000;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userUuid", user.uuid());
        return createToken(claims, user.username());
    }

    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
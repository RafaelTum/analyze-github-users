package com.example.analizegithubusers.security;

import com.example.analizegithubusers.model.Role;
import com.example.analizegithubusers.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${sec.jwt.signing-key}")
    private String jwtSigningKey;

    @Value("${sec.jwt.claim-name}")
    private String jwtClaimName;

    @Value("${sec.jwt.expiration}")
    private Long jwtExpiration;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSigningKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        final Set<String> authorities = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(jwtClaimName, authorities)
                .signWith(SignatureAlgorithm.HS256, jwtSigningKey)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .compact();
    }

}
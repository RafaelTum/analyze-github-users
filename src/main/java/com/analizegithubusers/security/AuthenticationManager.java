package com.analizegithubusers.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Value("${sec.jwt.claim-name}")
    private String jwtClaimName;

    private final JwtUtils jwtUtils;

    public AuthenticationManager(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username;
        try {
            username = jwtUtils.getUsernameFromToken(authToken);
        } catch (Exception e) {
            username = null;
        }
        if (username != null && !jwtUtils.isTokenExpired(authToken)) {
            Claims claims = jwtUtils.getAllClaimsFromToken(authToken);
            List<String> roles = claims.get(jwtClaimName, List.class);
            Set<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username, authorities);
            SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(username, authorities));
            return Mono.just(auth);
        } else {
            return Mono.empty();
        }
    }
}
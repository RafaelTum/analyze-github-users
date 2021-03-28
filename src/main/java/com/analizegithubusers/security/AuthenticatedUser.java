package com.analizegithubusers.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

public class AuthenticatedUser implements Authentication {

	private static final long serialVersionUID = 7206250620992576700L;

	private final String userId;
    private boolean authenticated = true;
    private final Set<SimpleGrantedAuthority> roles;

    public AuthenticatedUser(String userId, Set<SimpleGrantedAuthority> roles){
        this.userId = userId;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public Object getCredentials() {
        return this.userId;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated = b;
    }

    @Override
    public String getName() {
        return this.userId;
    }
}
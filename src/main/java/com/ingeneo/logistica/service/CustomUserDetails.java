package com.ingeneo.logistica.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

import com.ingeneo.logistica.model.User;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;
	private User user;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
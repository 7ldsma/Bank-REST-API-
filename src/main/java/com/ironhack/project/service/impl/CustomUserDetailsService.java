package com.ironhack.project.service.impl;

import com.ironhack.project.model.Role;
import com.ironhack.project.model.User;
import com.ironhack.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        // Check if user is present
        if(!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        // Create a list of authorities (roles)
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for(Role role : optionalUser.get().getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }

        // Return a Spring Security User with my User username, password and roles
        return new org.springframework.security.core.userdetails.User(
                optionalUser.get().getUsername(),
                optionalUser.get().getPassword(),
                authorities);
    }
}
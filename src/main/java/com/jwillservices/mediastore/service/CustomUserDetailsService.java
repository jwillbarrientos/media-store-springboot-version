package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.repository.ClientRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    public CustomUserDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword())
                .roles("USER")
                .build();
    }
}


package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.dto.LoginRequest;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.repository.ClientRepository;
import com.jwillservices.mediastore.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@RestController
public class AuthController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final AuthenticationManager authenticationManager;

    public AuthController(ClientService clientService, AuthenticationManager authenticationManager, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.authenticationManager = authenticationManager;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/public/login")
    @ResponseStatus(HttpStatus.OK)
    public Client login(@RequestBody LoginRequest request) {
        try {
            // Autenticar usando Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return clientRepository.findByEmail(request.getEmail());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @PostMapping("/public/signup")
    @ResponseStatus(HttpStatus.OK)
    public Client signUp(@RequestBody LoginRequest request) {
        Client client = clientService.createClient(new Client(request.getEmail(), request.getPassword()));

        // Autenticar automáticamente después del registro
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return client;
    }

    @PostMapping("/api/signout")
    @ResponseStatus(HttpStatus.OK)
    public void signOut() {
        SecurityContextHolder.clearContext();
    }

    @GetMapping("/api/getprofilename")
    @ResponseStatus(HttpStatus.OK)
    public Client getProfileName(UserDetails ud) {
        String email = ud.getUsername();
        return clientService.findByEmail(email);
    }
}
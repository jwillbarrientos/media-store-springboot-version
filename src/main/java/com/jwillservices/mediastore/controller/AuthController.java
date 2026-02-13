package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.dto.LoginRequest;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@RestController
public class AuthController {

    private final ClientService clientService;

    public AuthController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/public/login")
    @ResponseStatus(HttpStatus.OK)
    public Client login(@RequestBody LoginRequest request, HttpSession session) {
        Client client = clientService.findClientByEmailAndPassword(request.getEmail(), request.getPassword());
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        session.setAttribute("client", client);
        return client;
    }

    @PostMapping("/public/signup")
    @ResponseStatus(HttpStatus.OK)
    public Client signUp(@RequestBody LoginRequest request, HttpSession session) {
        Client client = clientService.createClient(new Client(request.getEmail(), request.getPassword()));
        session.setAttribute("client", client);
        return client;
    }

    @PostMapping("/api/signout")
    @ResponseStatus(HttpStatus.OK)
    public void signOut(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/api/getprofilename")
    @ResponseStatus(HttpStatus.OK)
    public Client getProfileName(HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user logged in");
        }
        return client;
    }
}
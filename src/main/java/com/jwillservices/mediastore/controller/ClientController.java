package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.dto.UpdatePasswordRequest;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.repository.ClientRepository;
import com.jwillservices.mediastore.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @GetMapping//(produces = "application/json")
    public List<Client> getAllClients() {
//        return Arrays.asList(new Client("jona@gmail.com", "super password"));
        return clientService.getAllClients();
    }

    @PatchMapping("/{id}/password")
    public Client updatePassword(@PathVariable Long id,
                                 @RequestBody UpdatePasswordRequest request) {
        return clientService.updatePassword(id, request.getNewPassword());
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
    }

}

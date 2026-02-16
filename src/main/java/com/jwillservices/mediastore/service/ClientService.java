package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Client updatePassword(Long id, String newPassword) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setPassword(passwordEncoder.encode(newPassword));
        return clientRepository.save(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public Client createClient(Client client) {
        // Codificar la contraseña con BCrypt
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }


    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }
}

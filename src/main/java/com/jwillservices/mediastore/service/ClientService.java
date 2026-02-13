package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client updatePassword(Long id, String newPassword) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setPassword(newPassword);
        return clientRepository.save(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    // todo borrar todo lo demas desde aca

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client findClientByEmailAndPassword(String email, String password) {
        return clientRepository.findClientByEmailAndPassword(email, password);
    }



    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }
}

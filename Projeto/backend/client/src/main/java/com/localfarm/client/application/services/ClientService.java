package com.localfarm.client.application.services;

import com.localfarm.client.domain.models.Client;
import com.localfarm.client.domain.models.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
        validateClient(client);
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client client) {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (existingClient.isPresent()) {
            client.setId(id);
            validateClient(client);
            return clientRepository.save(client);
        }
        throw new EntityNotFoundException("Client not found");
    }

    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client not found");
        }
        clientRepository.deleteById(id);
    }

    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    private void validateClient(Client client) {
        if (client.getType() == null) {
            throw new IllegalArgumentException("Client type is mandatory");
        }

        if (client.getCode() == null || client.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Client code is mandatory");
        }
    }
}
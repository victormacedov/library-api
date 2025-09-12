package com.victormacedov.library_api.service;

import com.victormacedov.library_api.model.Client;
import com.victormacedov.library_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public void salvar(Client client){
        client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
        clientRepository.save(client);
    }

    public Client obterPorClientId(String clientId){
        return clientRepository.findByClientId(clientId);
    }
}

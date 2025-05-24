package com.medvita.backend.services;

import com.medvita.backend.entities.Client;
import com.medvita.backend.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService extends AbstractService<Client> {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        super(clientRepository);
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public Client create(Client client) {
        if (clientRepository.existsByEmailAndActifTrue(client.getEmail())) {
            throw new RuntimeException("Un client avec cet email existe déjà");
        }
        if (client.getNumeroSecu() != null &&
                clientRepository.existsByNumeroSecuAndActifTrue(client.getNumeroSecu())) {
            throw new RuntimeException("Un client avec ce numéro de sécurité sociale existe déjà");
        }
        return super.create(client);
    }

    @Transactional
    public Client desactiverClient(Long id) {
        Client client = getById(id);
        client.setActif(false);
        return clientRepository.save(client);
    }

    public List<Client> rechercherClients(String recherche) {
        return clientRepository.rechercherClientsActifs(recherche);
    }

    public List<Client> obtenirClientsActifs() {
        return clientRepository.findAllByActifTrue();
    }

    public Client obtenirParEmail(String email) {
        return clientRepository.findByEmailAndActifTrue(email)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'email: " + email));
    }
}

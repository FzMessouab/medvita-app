package com.medvita.backend.repositories;


import com.medvita.backend.entities.Client;
import org.springframework.stereotype.Repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends AbstractRepository<
        Client,
        Long
        > {

    Optional<Client> findByEmailAndActifTrue(String email);

    boolean existsByEmailAndActifTrue(String email);

    boolean existsByNumeroSecuAndActifTrue(String numeroSecu);

    @Query("SELECT c FROM Client c WHERE c.actif = true AND " +
            "(LOWER(c.fullName) LIKE LOWER(CONCAT('%', :recherche, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :recherche, '%')))")
    List<Client> rechercherClientsActifs(String recherche);

    List<Client> findAllByActifTrue();
}
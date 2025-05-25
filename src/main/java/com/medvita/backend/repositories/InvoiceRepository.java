package com.medvita.backend.repositories;


import com.medvita.backend.entities.Invoice;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends AbstractRepository<Invoice,Long> {
}

package com.medvita.backend.services;

import com.medvita.backend.dto.BaseDTO;
import com.medvita.backend.dto.BaseResponseDTO;
import com.medvita.backend.entities.BaseEntity;
import com.medvita.backend.repositories.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractService<
        E extends BaseEntity<ID>,
        ID,
        D extends BaseDTO,
        R extends BaseResponseDTO,
        REPO extends AbstractRepository<E, ID>> {

    protected final REPO repository;

    protected AbstractService(REPO repository) {
        this.repository = repository;
    }

    @Transactional
    public E create(E entity) {
        beforeCreate(entity);
        E savedEntity = repository.save(entity);
        afterCreate(savedEntity);
        return savedEntity;
    }

    @Transactional
    public E update(E entity) {
        beforeUpdate(entity);
        E updatedEntity = repository.save(entity);
        afterUpdate(updatedEntity);
        return updatedEntity;
    }

    @Transactional
    public void delete(ID id) {
        beforeDelete(id);
        repository.softDelete(id);
        afterDelete(id);
    }

    @Transactional(readOnly = true)
    public Optional<E> findById(ID id) {
        return repository.findActiveById(id);
    }

    @Transactional(readOnly = true)
    public E getById(ID id) {
        return repository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Entité non trouvée avec l'ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<E> findAll() {
        return repository.findAllActive();
    }

    @Transactional(readOnly = true)
    public Page<E> findAll(Pageable pageable) {
        return repository.findAllByDeletedFalse(pageable);
    }

    // Hook methods for subclasses to override
    protected void beforeCreate(E entity) {}
    protected void afterCreate(E entity) {}
    protected void beforeUpdate(E entity) {}
    protected void afterUpdate(E entity) {}
    protected void beforeDelete(ID id) {}
    protected void afterDelete(ID id) {}
}

package com.medvita.backend.services;

import com.medvita.backend.entities.BaseEntity;
import com.medvita.backend.repositories.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<T extends BaseEntity> {

    protected final AbstractRepository<T> repository;

    public AbstractService(AbstractRepository<T> repository) {
        this.repository = repository;
    }

    @Transactional
    public T create(T entity) {
        beforeCreate(entity);
        T savedEntity = repository.save(entity);
        afterCreate(savedEntity);
        return savedEntity;
    }

    @Transactional
    public T update(T entity) {
        beforeUpdate(entity);
        T updatedEntity = repository.save(entity);
        afterUpdate(updatedEntity);
        return updatedEntity;
    }

    @Transactional
    public void delete(Long id) {
        beforeDelete(id);
        repository.softDelete(id);
        afterDelete(id);
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(Long id) {
        return repository.findActiveById(id);
    }

    @Transactional(readOnly = true)
    public T getById(Long id) {
        return repository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Entité non trouvée avec l'ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAllActive();
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return repository.findAllByDeletedFalse(pageable);
    }

    // Hook methods for subclasses to override
    protected void beforeCreate(T entity) {}
    protected void afterCreate(T entity) {}
    protected void beforeUpdate(T entity) {}
    protected void afterUpdate(T entity) {}
    protected void beforeDelete(Long id) {}
    protected void afterDelete(Long id) {}
}
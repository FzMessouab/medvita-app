package com.medvita.backend.controllers;

import com.medvita.backend.dto.BaseDTO;
import com.medvita.backend.dto.BaseResponseDTO;
import com.medvita.backend.entities.BaseEntity;
import com.medvita.backend.services.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public abstract class AbstractController<
        E extends BaseEntity<ID>,
        ID extends Serializable,
        D extends BaseDTO,
        R extends BaseResponseDTO,
        S extends AbstractService<E, ID, D, R, ?>>  {

    protected final S service;

    protected AbstractController(S service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<E>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<E>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<E> findById(@PathVariable ID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<E> create(@RequestBody E entity) {
        E created = service.create(entity);
        return ResponseEntity.created(URI.create("/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<E> update(@PathVariable ID id, @RequestBody E entity) {
        entity.setId(id);
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

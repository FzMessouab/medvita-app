package com.medvita.backend.repositories;

import com.medvita.backend.entities.BaseEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface AbstractRepository<E extends BaseEntity<ID>, ID>
        extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.deleted = false")
    Optional<E> findActiveById(@Param("id") ID id);

    @Query("SELECT e FROM #{#entityName} e WHERE e.deleted = false")
    List<E> findAllActive();

    @Query("UPDATE #{#entityName} e SET e.deleted = true WHERE e.id = :id")
    @Modifying
    @Transactional
    void softDelete(@Param("id") ID id);

    Page<E> findAllByDeletedFalse(Pageable pageable);
}
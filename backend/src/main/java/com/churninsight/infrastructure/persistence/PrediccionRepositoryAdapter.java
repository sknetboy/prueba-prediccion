package com.churninsight.infrastructure.persistence;

import com.churninsight.application.PrediccionRepositoryPort;
import com.churninsight.domain.Prediccion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrediccionRepositoryAdapter implements PrediccionRepositoryPort {
    private final PrediccionJpaRepository repository;

    public PrediccionRepositoryAdapter(PrediccionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Prediccion save(Prediccion prediccion) { return repository.save(prediccion); }

    @Override
    public List<Prediccion> findAll() { return repository.findAll(); }
}

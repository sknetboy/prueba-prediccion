package com.churninsight.infrastructure.persistence;

import com.churninsight.domain.Prediccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrediccionJpaRepository extends JpaRepository<Prediccion, Long> {}

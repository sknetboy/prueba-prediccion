package com.churninsight.application;

import com.churninsight.domain.Prediccion;
import java.util.List;

public interface PrediccionRepositoryPort {
    Prediccion save(Prediccion prediccion);
    List<Prediccion> findAll();
}

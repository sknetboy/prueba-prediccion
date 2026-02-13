package com.churninsight.application;

import com.churninsight.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PredecirChurnUseCase {
    private final ChurnPredictionPort predictionPort;
    private final PrediccionRepositoryPort repositoryPort;

    public PredecirChurnUseCase(ChurnPredictionPort predictionPort, PrediccionRepositoryPort repositoryPort) {
        this.predictionPort = predictionPort;
        this.repositoryPort = repositoryPort;
    }

    public ChurnPredictionPort.PredictionResponse execute(Cliente cliente) {
        var result = predictionPort.predict(cliente);
        String riesgo = result.probabilidad() >= 0.7 ? "Alto" : result.probabilidad() >= 0.4 ? "Medio" : "Bajo";
        repositoryPort.save(new Prediccion(result.prevision(), result.probabilidad(), riesgo));
        return result;
    }
}

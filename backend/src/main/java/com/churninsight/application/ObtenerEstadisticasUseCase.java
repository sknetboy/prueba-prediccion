package com.churninsight.application;

import org.springframework.stereotype.Service;

@Service
public class ObtenerEstadisticasUseCase {
    private final PrediccionRepositoryPort repositoryPort;

    public ObtenerEstadisticasUseCase(PrediccionRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public StatsResponse execute() {
        var all = repositoryPort.findAll();
        int total = all.size();
        long churn = all.stream().filter(p -> "Va a cancelar".equalsIgnoreCase(p.getPrevision())).count();
        double tasa = total == 0 ? 0.0 : (double) churn / total;
        return new StatsResponse(total, Math.round(tasa * 100.0) / 100.0);
    }

    public record StatsResponse(int totalEvaluados, double tasaChurn) {}
}

package com.churninsight.domain;

public interface ChurnPredictionPort {
    PredictionResponse predict(Cliente cliente);

    record PredictionResponse(String prevision, double probabilidad, java.util.List<String> factoresClave) {}
}

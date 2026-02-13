package com.churninsight.infrastructure.client;

import com.churninsight.domain.ChurnPredictionPort;
import com.churninsight.domain.Cliente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class PythonPredictionClientAdapter implements ChurnPredictionPort {
    private final RestClient restClient;

    public PythonPredictionClientAdapter(@Value("${ds.service.url:http://localhost:8000}") String dsServiceUrl) {
        this.restClient = RestClient.builder().baseUrl(dsServiceUrl).build();
    }

    @Override
    public PredictionResponse predict(Cliente cliente) {
        Map<String, Object> body = Map.of(
                "tiempo_contrato_meses", cliente.tiempoContratoMeses(),
                "retrasos_pago", cliente.retrasosPago(),
                "uso_mensual", cliente.usoMensual(),
                "plan", cliente.plan());

        Map response = restClient.post()
                .uri("/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Map.class);

        String prevision = (String) response.get("prevision");
        double probabilidad = ((Number) response.get("probabilidad")).doubleValue();
        List<String> factores = (List<String>) response.getOrDefault("factores_clave", List.of());
        return new PredictionResponse(prevision, probabilidad, factores);
    }
}

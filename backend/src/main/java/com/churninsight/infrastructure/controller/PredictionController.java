package com.churninsight.infrastructure.controller;

import com.churninsight.application.ObtenerEstadisticasUseCase;
import com.churninsight.application.PredecirChurnUseCase;
import com.churninsight.domain.Cliente;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PredictionController {
    private final PredecirChurnUseCase predecir;
    private final ObtenerEstadisticasUseCase stats;

    public PredictionController(PredecirChurnUseCase predecir, ObtenerEstadisticasUseCase stats) {
        this.predecir = predecir;
        this.stats = stats;
    }

    @PostMapping("/predict")
    public Object predict(@RequestBody PredictRequest request) {
        var result = predecir.execute(new Cliente(request.tiempoContratoMeses(), request.retrasosPago(), request.usoMensual(), request.plan()));
        return java.util.Map.of("prevision", result.prevision(), "probabilidad", result.probabilidad(), "factores_clave", result.factoresClave());
    }

    @GetMapping("/stats")
    public Object stats() {
        var result = stats.execute();
        return java.util.Map.of("total_evaluados", result.totalEvaluados(), "tasa_churn", result.tasaChurn());
    }

    public record PredictRequest(@Min(0) int tiempoContratoMeses, @Min(0) int retrasosPago, @Min(0) double usoMensual, @NotBlank String plan) {}
}

from dataclasses import dataclass

import numpy as np

from app.domain.entities import Cliente


@dataclass(frozen=True)
class PrediccionChurn:
    prevision: str
    probabilidad: float
    factores_clave: list[str]


class ChurnPredictor:
    def __init__(self, model_repository):
        self.model_repository = model_repository

    def predecir(self, cliente: Cliente) -> PrediccionChurn:
        pipeline = self.model_repository.load_model()
        row = np.array(
            [[
                cliente.tiempo_contrato_meses,
                cliente.historial_pago.retrasos_pago,
                cliente.metricas_uso.uso_mensual,
                cliente.plan,
            ]],
            dtype=object,
        )
        proba = float(pipeline.predict_proba(row)[0][1])
        prevision = "Va a cancelar" if proba >= 0.5 else "Va a continuar"
        factores = self.model_repository.top_features(3)
        return PrediccionChurn(prevision=prevision, probabilidad=round(proba, 2), factores_clave=factores)

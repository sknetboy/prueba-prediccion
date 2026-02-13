from dataclasses import dataclass


@dataclass(frozen=True)
class MetricasUso:
    uso_mensual: float


@dataclass(frozen=True)
class HistorialPago:
    retrasos_pago: int


@dataclass(frozen=True)
class Cliente:
    tiempo_contrato_meses: int
    metricas_uso: MetricasUso
    historial_pago: HistorialPago
    plan: str

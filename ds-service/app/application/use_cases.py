from app.domain.entities import Cliente, HistorialPago, MetricasUso
from app.domain.services import ChurnPredictor


class PredecirChurnUseCase:
    def __init__(self, predictor: ChurnPredictor):
        self.predictor = predictor

    def execute(self, payload: dict):
        cliente = Cliente(
            tiempo_contrato_meses=payload["tiempo_contrato_meses"],
            metricas_uso=MetricasUso(uso_mensual=payload["uso_mensual"]),
            historial_pago=HistorialPago(retrasos_pago=payload["retrasos_pago"]),
            plan=payload["plan"],
        )
        return self.predictor.predecir(cliente)

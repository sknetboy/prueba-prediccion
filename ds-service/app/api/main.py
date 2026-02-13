from fastapi import FastAPI
from pydantic import BaseModel, Field

from app.application.use_cases import PredecirChurnUseCase
from app.domain.services import ChurnPredictor
from app.infrastructure.model_repository import JoblibModelRepository

app = FastAPI(title="ChurnInsight DS Service", version="1.0.0")

repository = JoblibModelRepository()
predictor = ChurnPredictor(repository)
use_case = PredecirChurnUseCase(predictor)


class PredictRequest(BaseModel):
    tiempo_contrato_meses: int = Field(ge=0)
    retrasos_pago: int = Field(ge=0)
    uso_mensual: float = Field(ge=0)
    plan: str


@app.post("/predict")
def predict(payload: PredictRequest):
    prediction = use_case.execute(payload.model_dump())
    return {
        "prevision": prediction.prevision,
        "probabilidad": prediction.probabilidad,
        "factores_clave": prediction.factores_clave,
    }

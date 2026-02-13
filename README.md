# ChurnInsight MVP

Plataforma de predicción de churn con arquitectura hexagonal en tres capas: DS (FastAPI + scikit-learn), Backend (Spring Boot) y Frontend (React + Vite).

## Arquitectura
- **DS service**: dominio (`Cliente`), caso de uso (`PredecirChurnUseCase`), repositorio de modelo y endpoint `/predict`.
- **Backend**: puertos (`ChurnPredictionPort`, `PrediccionRepositoryPort`), casos de uso (`Predecir`, `ObtenerEstadisticas`) y REST `/api/v1`.
- **Frontend**: arquitectura por features, hooks personalizados y capa Axios.

## Contrato de integración
Se define en `integration-contract.json` y se comparte entre equipos.

## Ejecución local
1. DS service:
   ```bash
   cd ds-service && pip install -r requirements.txt && uvicorn app.api.main:app --reload
   ```
2. Backend:
   ```bash
   cd backend && mvn spring-boot:run
   ```
3. Frontend:
   ```bash
   cd frontend && npm install && npm run dev
   ```

## Docker Compose
```bash
docker compose up --build
```

## Ejemplos curl
```bash
curl -X POST http://localhost:8000/predict -H 'Content-Type: application/json' -d '{"tiempo_contrato_meses":12,"retrasos_pago":2,"uso_mensual":14.5,"plan":"Premium"}'

curl -X POST http://localhost:8080/api/v1/predict -H 'Content-Type: application/json' -d '{"tiempoContratoMeses":12,"retrasosPago":2,"usoMensual":14.5,"plan":"Premium"}'

curl http://localhost:8080/api/v1/stats
```

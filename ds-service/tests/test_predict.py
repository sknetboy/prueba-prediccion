from fastapi.testclient import TestClient

from app.api.main import app


def test_predict_endpoint():
    client = TestClient(app)
    response = client.post(
        "/predict",
        json={
            "tiempo_contrato_meses": 12,
            "retrasos_pago": 3,
            "uso_mensual": 10.5,
            "plan": "Premium",
        },
    )
    assert response.status_code == 200
    payload = response.json()
    assert "prevision" in payload
    assert "probabilidad" in payload
    assert isinstance(payload["factores_clave"], list)

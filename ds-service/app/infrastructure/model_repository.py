from pathlib import Path

import joblib
import numpy as np
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, f1_score, precision_score, recall_score
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder

from app.application.ports import ModeloRepositoryPort

MODEL_PATH = Path(__file__).resolve().parents[2] / "churn_model.pkl"


class JoblibModelRepository(ModeloRepositoryPort):
    def __init__(self, model_path: Path = MODEL_PATH):
        self.model_path = model_path
        if not self.model_path.exists():
            self.train_and_save_model()
        self.artifact = joblib.load(self.model_path)

    def load_model(self):
        return self.artifact["pipeline"]

    def top_features(self, top_n: int) -> list[str]:
        importances = self.artifact["feature_importance"]
        sorted_items = sorted(importances.items(), key=lambda x: x[1], reverse=True)
        return [name for name, _ in sorted_items[:top_n]]

    def train_and_save_model(self):
        df = self._create_dataset()
        X = df[["tiempo_contrato_meses", "retrasos_pago", "uso_mensual", "plan"]]
        y = df["churn"]

        preprocessor = ColumnTransformer(
            transformers=[
                ("cat", OneHotEncoder(handle_unknown="ignore"), ["plan"]),
                ("num", "passthrough", ["tiempo_contrato_meses", "retrasos_pago", "uso_mensual"]),
            ]
        )
        pipeline = Pipeline(
            steps=[
                ("preprocessor", preprocessor),
                ("classifier", RandomForestClassifier(n_estimators=200, random_state=42)),
            ]
        )

        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
        pipeline.fit(X_train, y_train)
        preds = pipeline.predict(X_test)

        metrics = {
            "accuracy": float(accuracy_score(y_test, preds)),
            "precision": float(precision_score(y_test, preds)),
            "recall": float(recall_score(y_test, preds)),
            "f1": float(f1_score(y_test, preds)),
        }

        feature_names = pipeline.named_steps["preprocessor"].get_feature_names_out()
        importances = pipeline.named_steps["classifier"].feature_importances_
        grouped = {
            "tiempo_contrato_meses": 0.0,
            "retrasos_pago": 0.0,
            "uso_mensual": 0.0,
            "plan": 0.0,
        }
        for name, value in zip(feature_names, importances):
            if "plan" in name:
                grouped["plan"] += float(value)
            elif "tiempo_contrato_meses" in name:
                grouped["tiempo_contrato_meses"] += float(value)
            elif "retrasos_pago" in name:
                grouped["retrasos_pago"] += float(value)
            elif "uso_mensual" in name:
                grouped["uso_mensual"] += float(value)

        artifact = {"pipeline": pipeline, "metrics": metrics, "feature_importance": grouped}
        joblib.dump(artifact, self.model_path)

    def _create_dataset(self) -> pd.DataFrame:
        rng = np.random.default_rng(42)
        size = 1500
        contract = rng.integers(1, 60, size)
        delays = rng.integers(0, 8, size)
        usage = rng.normal(20, 8, size).clip(1, 45)
        plan = rng.choice(["Basic", "Standard", "Premium"], size=size, p=[0.4, 0.35, 0.25])

        risk = (
            0.35 * (delays / 8)
            + 0.3 * (1 - (contract / 60))
            + 0.25 * (1 - (usage / 45))
            + 0.1 * np.where(plan == "Basic", 0.8, np.where(plan == "Standard", 0.5, 0.2))
        )
        churn = (risk > 0.45).astype(int)

        return pd.DataFrame(
            {
                "tiempo_contrato_meses": contract,
                "retrasos_pago": delays,
                "uso_mensual": usage,
                "plan": plan,
                "churn": churn,
            }
        )

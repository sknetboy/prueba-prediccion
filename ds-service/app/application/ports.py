from abc import ABC, abstractmethod


class ModeloRepositoryPort(ABC):
    @abstractmethod
    def load_model(self):
        raise NotImplementedError

    @abstractmethod
    def top_features(self, top_n: int) -> list[str]:
        raise NotImplementedError

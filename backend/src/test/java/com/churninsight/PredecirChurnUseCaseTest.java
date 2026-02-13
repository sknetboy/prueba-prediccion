package com.churninsight;

import com.churninsight.application.PredecirChurnUseCase;
import com.churninsight.application.PrediccionRepositoryPort;
import com.churninsight.domain.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PredecirChurnUseCaseTest {
    @Test
    void asignaRiesgoYGuarda() {
        ChurnPredictionPort predictionPort = cliente -> new ChurnPredictionPort.PredictionResponse("Va a cancelar", 0.8, List.of("retrasos_pago"));
        PrediccionRepositoryPort repository = Mockito.mock(PrediccionRepositoryPort.class);
        Mockito.when(repository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        var useCase = new PredecirChurnUseCase(predictionPort, repository);
        var response = useCase.execute(new Cliente(10, 4, 12.0, "Premium"));

        assertEquals("Va a cancelar", response.prevision());
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }
}

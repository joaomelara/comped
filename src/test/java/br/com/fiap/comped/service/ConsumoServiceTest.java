package br.com.fiap.comped.service;

import br.com.fiap.comped.dto.ConsumoDTO;
import br.com.fiap.comped.model.ConsumoEnergia;
import br.com.fiap.comped.model.EquipamentoMonitorado;
import br.com.fiap.comped.repository.ConsumoRepository;
import br.com.fiap.comped.repository.EquipamentoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsumoServiceTest {

    private final ConsumoRepository consumoRepository = mock(ConsumoRepository.class);
    private final EquipamentoRepository equipamentoRepository = mock(EquipamentoRepository.class);

    private final ConsumoService service =
            new ConsumoService(consumoRepository, equipamentoRepository);

    @Test
    void deveRegistrarConsumoComSucesso() {
        EquipamentoMonitorado eqp = new EquipamentoMonitorado();
        eqp.setIdEquipamento(1L);
        eqp.setLimiteKwh(100.0);

        ConsumoDTO dto = new ConsumoDTO(1L, Instant.now(), 50.0);

        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(eqp));
        when(consumoRepository.existsByEquipamento_IdEquipamentoAndDataConsumo(any(), any()))
                .thenReturn(false);
        when(consumoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ConsumoEnergia result = service.registrar(dto);

        assertNotNull(result);
        assertEquals(50.0, result.getKwhConsumo());
    }

    @Test
    void deveLancarErroSeEquipamentoNaoExiste() {
        ConsumoDTO dto = new ConsumoDTO(1L, Instant.now(), 50.0);

        when(equipamentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.registrar(dto));
    }

    @Test
    void deveLancarErroSeConsumoDuplicado() {
        EquipamentoMonitorado eqp = new EquipamentoMonitorado();

        ConsumoDTO dto = new ConsumoDTO(1L, Instant.now(), 50.0);

        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(eqp));
        when(consumoRepository.existsByEquipamento_IdEquipamentoAndDataConsumo(any(), any()))
                .thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.registrar(dto));
    }
}
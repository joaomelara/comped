package br.com.fiap.comped.service;

import br.com.fiap.comped.dto.EquipamentoDTO;
import br.com.fiap.comped.dto.EquipamentoUpdateDTO;
import br.com.fiap.comped.model.EquipamentoMonitorado;
import br.com.fiap.comped.model.SetorEmpresa;
import br.com.fiap.comped.repository.EquipamentoRepository;
import br.com.fiap.comped.repository.SetorRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipamentoServiceTest {

    private final EquipamentoRepository equipamentoRepository = mock(EquipamentoRepository.class);
    private final SetorRepository setorRepository = mock(SetorRepository.class);

    private final EquipamentoService service =
            new EquipamentoService(equipamentoRepository, setorRepository);

    @Test
    void deveCriarEquipamento() {
        SetorEmpresa setor = new SetorEmpresa();
        setor.setIdSetor(1L);

        EquipamentoDTO dto = new EquipamentoDTO(
                1L,
                "Ar condicionado",
                LocalDate.now(),
                100.0
        );

        when(setorRepository.findById(1L)).thenReturn(Optional.of(setor));
        when(equipamentoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EquipamentoMonitorado result = service.criar(dto);

        assertNotNull(result);
        assertEquals("Ar condicionado", result.getNomeEquipamento());
        assertEquals(1, result.getAtivo());
    }

    @Test
    void deveAtualizarEquipamento() {
        EquipamentoMonitorado eqp = new EquipamentoMonitorado();
        eqp.setIdEquipamento(1L);
        eqp.setNomeEquipamento("Antigo");

        EquipamentoUpdateDTO dto = new EquipamentoUpdateDTO(
                "Novo Nome",
                200.0,
                true
        );

        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(eqp));
        when(equipamentoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EquipamentoMonitorado result = service.atualizar(1L, dto);

        assertEquals("Novo Nome", result.getNomeEquipamento());
        assertEquals(200.0, result.getLimiteKwh());
        assertEquals(1, result.getAtivo());
    }

    @Test
    void deveLancarErroSeSetorNaoExiste() {
        EquipamentoDTO dto = new EquipamentoDTO(
                1L,
                "Equipamento",
                LocalDate.now(),
                50.0
        );

        when(setorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.criar(dto));
    }
}
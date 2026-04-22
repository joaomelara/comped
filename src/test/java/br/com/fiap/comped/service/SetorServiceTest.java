package br.com.fiap.comped.service;

import br.com.fiap.comped.dto.SetorDTO;
import br.com.fiap.comped.model.SetorEmpresa;
import br.com.fiap.comped.repository.SetorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SetorServiceTest {

    private final SetorRepository repository = mock(SetorRepository.class);

    private final SetorService service = new SetorService(repository);

    @Test
    void deveCriarSetor() {
        SetorDTO dto = new SetorDTO("TI");

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        SetorEmpresa result = service.criar(dto);

        assertNotNull(result);
        assertEquals("TI", result.getNomeSetor());
    }

    @Test
    void deveListarTodosQuandoNomeNulo() {
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable))
                .thenReturn(new PageImpl<>(java.util.List.of(new SetorEmpresa())));

        Page<SetorEmpresa> result = service.listar(null, pageable);

        assertFalse(result.isEmpty());
    }

    @Test
    void deveBuscarPorNome() {
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByNomeSetorContainingIgnoreCase("TI", pageable))
                .thenReturn(new PageImpl<>(java.util.List.of(new SetorEmpresa())));

        Page<SetorEmpresa> result = service.listar("TI", pageable);

        assertFalse(result.isEmpty());
    }
}
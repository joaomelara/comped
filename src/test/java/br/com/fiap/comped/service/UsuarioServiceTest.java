package br.com.fiap.comped.service;

import br.com.fiap.comped.dto.UsuarioCadastroDTO;
import br.com.fiap.comped.dto.UsuarioExibicaoDTO;
import br.com.fiap.comped.model.Usuario;
import br.com.fiap.comped.model.UsuarioRole;
import br.com.fiap.comped.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private final UsuarioRepository repository = mock(UsuarioRepository.class);

    private final UsuarioService service =
            new UsuarioService(repository);

    @Test
    void deveSalvarUsuario() {
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO(
                null,
                "João",
                "joao@email.com",
                "123456",
                UsuarioRole.USER
        );

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        UsuarioExibicaoDTO result = service.salvarUsuario(dto);

        assertNotNull(result);
        assertEquals("João", result.nomeUsuario());
        assertEquals("joao@email.com", result.emailUsuario());
    }

    @Test
    void deveBuscarPorId() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNomeUsuario("João");
        usuario.setEmailUsuario("email@email.com");

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioExibicaoDTO result = service.buscarPorId(1L);

        assertEquals("João", result.nomeUsuario());
    }

    @Test
    void deveListarUsuarios() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNomeUsuario("João");
        usuario.setEmailUsuario("email@email.com");

        when(repository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioExibicaoDTO> lista = service.listarTodos();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    void deveExcluirUsuario() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        service.excluir(1L);

        verify(repository).delete(usuario);
    }

    @Test
    void deveAtualizarUsuario() {
        Usuario existente = new Usuario();
        existente.setIdUsuario(1L);
        existente.setSenhaUsuario("senhaAntiga");

        Usuario atualizado = new Usuario();
        atualizado.setIdUsuario(1L);
        atualizado.setNomeUsuario("Novo Nome");
        atualizado.setSenhaUsuario("123456");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario result = service.atualizar(atualizado);

        assertEquals("Novo Nome", result.getNomeUsuario());
        assertNotEquals("123456", result.getSenhaUsuario()); // senha criptografada
    }

    @Test
    void deveManterSenhaSeNaoInformada() {
        Usuario existente = new Usuario();
        existente.setIdUsuario(1L);
        existente.setSenhaUsuario("senhaAntiga");

        Usuario atualizado = new Usuario();
        atualizado.setIdUsuario(1L);
        atualizado.setNomeUsuario("Novo Nome");
        atualizado.setSenhaUsuario(null);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario result = service.atualizar(atualizado);

        assertEquals("senhaAntiga", result.getSenhaUsuario());
    }
}
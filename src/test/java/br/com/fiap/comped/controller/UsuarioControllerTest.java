package br.com.fiap.comped.controller;

import br.com.fiap.comped.config.security.VerificarToken;
import br.com.fiap.comped.dto.UsuarioCadastroDTO;
import br.com.fiap.comped.dto.UsuarioExibicaoDTO;
import br.com.fiap.comped.model.Usuario;
import br.com.fiap.comped.model.UsuarioRole;
import br.com.fiap.comped.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UsuarioController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = VerificarToken.class
        )
)

@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarUsuario() throws Exception {
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO(
                null,
                "João",
                "joao@email.com",
                "123456",
                UsuarioRole.USER
        );

        when(service.salvarUsuario(any()))
                .thenReturn(new UsuarioExibicaoDTO(1L, "João", "joao@email.com"));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveListarUsuarios() throws Exception {
        when(service.listarTodos())
                .thenReturn(List.of(new UsuarioExibicaoDTO(1L, "João", "email@email.com")));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorId() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(new UsuarioExibicaoDTO(1L, "João", "email@email.com"));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveExcluirUsuario() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO(
                null,
                "Novo Nome",
                "novo@email.com",
                "123456",
                UsuarioRole.USER
        );

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNomeUsuario("Novo Nome");
        usuario.setEmailUsuario("novo@email.com");

        when(service.atualizar(any())).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
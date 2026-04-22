package br.com.fiap.comped.controller;

import br.com.fiap.comped.config.security.VerificarToken;
import br.com.fiap.comped.dto.SetorDTO;
import br.com.fiap.comped.model.SetorEmpresa;
import br.com.fiap.comped.service.SetorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = SetorController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = VerificarToken.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class SetorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SetorService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarSetores() throws Exception {
        when(service.listar(null, org.springframework.data.domain.PageRequest.of(0,10)))
                .thenReturn(new PageImpl<>(List.of(new SetorEmpresa())));

        mockMvc.perform(get("/setores"))
                .andExpect(status().isOk());
    }

    @Test
    void deveCriarSetor() throws Exception {
        SetorDTO dto = new SetorDTO("Financeiro");

        when(service.criar(dto)).thenReturn(new SetorEmpresa());

        mockMvc.perform(post("/setores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}
package br.com.fiap.comped.controller;

import br.com.fiap.comped.config.security.VerificarToken;
import br.com.fiap.comped.dto.EquipamentoDTO;
import br.com.fiap.comped.dto.EquipamentoUpdateDTO;
import br.com.fiap.comped.model.EquipamentoMonitorado;
import br.com.fiap.comped.service.EquipamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = EquipamentoController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = VerificarToken.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class EquipamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipamentoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarEquipamentos() throws Exception {
        when(service.listar(null, null, org.springframework.data.domain.PageRequest.of(0,10)))
                .thenReturn(new PageImpl<>(List.of(new EquipamentoMonitorado())));

        mockMvc.perform(get("/equipamentos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveCriarEquipamento() throws Exception {
        EquipamentoDTO dto = new EquipamentoDTO(
                1L,
                "Ar condicionado",
                LocalDate.now(),
                100.0
        );

        when(service.criar(dto)).thenReturn(new EquipamentoMonitorado());

        mockMvc.perform(post("/equipamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveAtualizarEquipamento() throws Exception {
        EquipamentoUpdateDTO dto = new EquipamentoUpdateDTO(
                "Novo Nome",
                200.0,
                true
        );

        when(service.atualizar(1L, dto)).thenReturn(new EquipamentoMonitorado());

        mockMvc.perform(put("/equipamentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
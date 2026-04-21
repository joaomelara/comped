package br.com.fiap.comped.controller;

import br.com.fiap.comped.config.security.VerificarToken;
import br.com.fiap.comped.dto.ConsumoDTO;
import br.com.fiap.comped.model.ConsumoEnergia;
import br.com.fiap.comped.service.ConsumoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(
        controllers = ConsumoController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = VerificarToken.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class ConsumoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsumoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarConsumos() throws Exception {
        when(service.listar(1L, null, null))
                .thenReturn(List.of(new ConsumoEnergia()));

        mockMvc.perform(get("/consumos")
                        .param("equipId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveRegistrarConsumo() throws Exception {
        ConsumoDTO dto = new ConsumoDTO(1L, Instant.now(), 20.0);

        when(service.registrar(dto)).thenReturn(new ConsumoEnergia());

        mockMvc.perform(post("/consumos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}
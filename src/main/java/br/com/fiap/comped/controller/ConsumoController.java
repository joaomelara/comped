package br.com.fiap.comped.controller;

import br.com.fiap.comped.dto.ConsumoDTO;
import br.com.fiap.comped.model.ConsumoEnergia;
import br.com.fiap.comped.service.ConsumoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/consumos")
public class ConsumoController {
    private final ConsumoService consumoService;

    public ConsumoController(ConsumoService consumoService) {
        this.consumoService = consumoService;
    }

    @GetMapping
    public List<ConsumoEnergia> listar(
            @RequestParam Long equipId,
            @RequestParam(required = false) Instant inicio,
            @RequestParam(required = false) Instant fim) {
        return consumoService.listar(equipId, inicio, fim);
    }

    @PostMapping
    public ResponseEntity<ConsumoEnergia> registrar(@Valid @RequestBody ConsumoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consumoService.registrar(dto));
    }
}
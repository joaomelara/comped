package br.com.fiap.comped.controller;

import br.com.fiap.comped.dto.EquipamentoDTO;
import br.com.fiap.comped.dto.EquipamentoUpdateDTO;
import br.com.fiap.comped.model.EquipamentoMonitorado;
import br.com.fiap.comped.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {
    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @GetMapping
    public Page<EquipamentoMonitorado> listar(
            @RequestParam(required = false) Long setorId,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return equipamentoService.listar(setorId, ativo, PageRequest.of(page, size));
    }

    @PostMapping
    public ResponseEntity<EquipamentoMonitorado> criar(@Valid @RequestBody EquipamentoDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoMonitorado> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipamentoUpdateDTO request) {
        return ResponseEntity.ok(equipamentoService.atualizar(id, request));
    }
}

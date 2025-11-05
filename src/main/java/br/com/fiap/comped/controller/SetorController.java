package br.com.fiap.comped.controller;

import br.com.fiap.comped.dto.SetorDTO;
import br.com.fiap.comped.model.SetorEmpresa;
import br.com.fiap.comped.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/setores")
public class SetorController {
    private final SetorService setorService;

    public SetorController(SetorService setorService) {
        this.setorService = setorService;
    }

    @GetMapping
    public Page<SetorEmpresa> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return setorService.listar(nome, PageRequest.of(page, size));
    }

    @PostMapping
    public ResponseEntity<SetorEmpresa> criar(@Valid @RequestBody SetorDTO request) {
        SetorEmpresa setor = setorService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(setor);
    }
}

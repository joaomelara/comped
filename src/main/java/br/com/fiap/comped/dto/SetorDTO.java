package br.com.fiap.comped.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetorDTO(
        @NotBlank @Size(max = 50)
        String nomeSetor
) {}

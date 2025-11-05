package br.com.fiap.comped.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EquipamentoDTO(
        @NotNull Long setorId,
        @NotBlank @Size(max = 100) String nomeEquipamento,
        @PastOrPresent LocalDate dataInstalacao,
        @Min(0) Double limiteKwh
) {}


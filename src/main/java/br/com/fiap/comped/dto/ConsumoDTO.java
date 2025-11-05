package br.com.fiap.comped.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;


    public record ConsumoDTO(
            @NotNull Long equipId,
            @NotNull Instant dataConsumo,
            @DecimalMin("0.0") Double kwhConsumo
    ) {}


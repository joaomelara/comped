package br.com.fiap.comped.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record EquipamentoUpdateDTO(
        @Size(max = 100) String nomeEquipamento,
        @Min(0) Double limiteKwh,
        Boolean ativo
){}

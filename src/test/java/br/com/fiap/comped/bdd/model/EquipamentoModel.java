package br.com.fiap.comped.bdd.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class EquipamentoModel {

    @Expose(serialize = false)
    private Long idEquipamento;

    @Expose
    private Long setorId;

    @Expose
    private String nomeEquipamento;

    @Expose
    private String dataInstalacao;

    @Expose
    private Double limiteKwh;

    @Expose
    private Boolean ativo;
}


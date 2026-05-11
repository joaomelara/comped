package br.com.fiap.comped.bdd.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class EquipamentoModel {

    @Expose(serialize = false)
    private Long id;

    @Expose
    private String nomeEquipamento;

    @Expose
    private Double consumoKwh;

    @Expose
    private Boolean ativo;

    @Expose
    private Long setorId;
}
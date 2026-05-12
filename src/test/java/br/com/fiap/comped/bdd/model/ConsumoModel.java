package br.com.fiap.comped.bdd.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class ConsumoModel {

    @Expose(serialize = false)
    private Long idConsumo;

    @Expose
    private Long equipId;

    @Expose
    private String dataConsumo;

    @Expose
    private Double kwhConsumo;

}
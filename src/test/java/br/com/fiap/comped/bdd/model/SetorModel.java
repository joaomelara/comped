package br.com.fiap.comped.bdd.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class SetorModel {
    @Expose(serialize = false)
    private long idSetor;
    @Expose
    private String nomeSetor;
}

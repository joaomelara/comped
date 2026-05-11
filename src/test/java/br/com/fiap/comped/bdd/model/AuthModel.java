package br.com.fiap.comped.bdd.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class AuthModel {

    @Expose
    private String emailUsuario;

    @Expose
    private String senhaUsuario;

    @Expose
    private String nomeUsuario;

    @Expose
    private String role;
}
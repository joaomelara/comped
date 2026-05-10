package br.com.fiap.comped.bdd.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class UsuarioModel {
    @Expose(serialize = false)
    private int idUsuario;
    @Expose
    private String nomeUsuario;
    @Expose
    private String emailUsuario;
    @Expose
    private String senhaUsuario;
    @Expose
    private String role;
}
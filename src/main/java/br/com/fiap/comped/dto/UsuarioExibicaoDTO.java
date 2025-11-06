package br.com.fiap.comped.dto;

import br.com.fiap.comped.model.Usuario;

public record UsuarioExibicaoDTO(
        Long idUsuario,
        String nomeUsuario,
        String emailUsuario
) {
    public UsuarioExibicaoDTO(Usuario usuario) {
        this(
                usuario.getIdUsuario(),
                usuario.getNomeUsuario(),
                usuario.getEmailUsuario()
        );
    }
}

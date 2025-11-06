package br.com.fiap.comped.dto;

import br.com.fiap.comped.model.UsuarioRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroDTO(

        Long idUsuario,

        @NotBlank(message = "O nome do usuário é obrigatório!")
        String nomeUsuario,

        @NotBlank(message = "O e-mail do usuário é obrigatório!")
        @Email(message = "O e-mail do usuário não é válido!")
        String emailUsuario,

        @NotBlank(message = "A senha é obrigatória!")
        @Size(min = 6, max = 20, message = "A senha deve conter entre 6 e 20 caracteres!")
        String senhaUsuario,

        UsuarioRole role
) {
}

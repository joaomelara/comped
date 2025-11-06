package br.com.fiap.comped.repository;

import br.com.fiap.comped.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByEmailUsuario(String emailUsuario);

}

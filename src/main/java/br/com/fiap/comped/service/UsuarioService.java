package br.com.fiap.comped.service;

import br.com.fiap.comped.dto.UsuarioCadastroDTO;
import br.com.fiap.comped.dto.UsuarioExibicaoDTO;
import br.com.fiap.comped.exception.UsuarioNaoEncontradoException;
import br.com.fiap.comped.model.Usuario;
import br.com.fiap.comped.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioExibicaoDTO salvarUsuario(UsuarioCadastroDTO usuarioDTO) {
        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDTO.senhaUsuario());

        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);
        usuario.setSenhaUsuario(senhaCriptografada);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return new UsuarioExibicaoDTO(usuarioSalvo);
    }

    public UsuarioExibicaoDTO buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioExibicaoDTO::new)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não existe no banco de dados!"));
    }

    /*
    public UsuarioExibicaoDTO buscarPorEmail(String email) {
        return usuarioRepository.findByEmailUsuario(email)
                .map(UsuarioExibicaoDTO::new)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não existe no banco de dados!"));
    }
    */

    public List<UsuarioExibicaoDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioExibicaoDTO::new)
                .toList();
    }

    public void excluir(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));

        usuarioRepository.delete(usuario);
    }

    public Usuario atualizar(Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioAtualizado.getIdUsuario())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));

        // Evita sobrescrever a senha original se ela não for enviada
        if (usuarioAtualizado.getSenhaUsuario() == null || usuarioAtualizado.getSenhaUsuario().isBlank()) {
            usuarioAtualizado.setSenhaUsuario(usuarioExistente.getSenhaUsuario());
        } else {
            String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioAtualizado.getSenhaUsuario());
            usuarioAtualizado.setSenhaUsuario(senhaCriptografada);
        }

        BeanUtils.copyProperties(usuarioAtualizado, usuarioExistente, "idUsuario");
        return usuarioRepository.save(usuarioExistente);
    }
}

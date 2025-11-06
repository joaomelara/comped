package br.com.fiap.comped.controller;

import br.com.fiap.comped.dto.UsuarioCadastroDTO;
import br.com.fiap.comped.dto.UsuarioExibicaoDTO;
import br.com.fiap.comped.exception.UsuarioNaoEncontradoException;
import br.com.fiap.comped.model.Usuario;
import br.com.fiap.comped.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioExibicaoDTO salvar(@RequestBody @Valid UsuarioCadastroDTO usuarioDTO) {
        return usuarioService.salvarUsuario(usuarioDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioExibicaoDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioExibicaoDTO> buscarPorId(@PathVariable Long idUsuario) {
        UsuarioExibicaoDTO usuario = usuarioService.buscarPorId(idUsuario);
        return ResponseEntity.ok(usuario);
    }

    /*
    @GetMapping(params = "emailUsuario")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioExibicaoDTO buscarPorEmail(@RequestParam String emailUsuario) {
        return usuarioService.buscarPorEmail(emailUsuario);
    }
    */

    @DeleteMapping("/{idUsuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long idUsuario) {
        usuarioService.excluir(idUsuario);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioExibicaoDTO> atualizar(
            @PathVariable Long idUsuario,
            @RequestBody @Valid UsuarioCadastroDTO usuarioDTO) {

        // Criar um objeto Usuario com os dados atualizados
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNomeUsuario(usuarioDTO.nomeUsuario());
        usuario.setEmailUsuario(usuarioDTO.emailUsuario());
        usuario.setSenhaUsuario(usuarioDTO.senhaUsuario());
        usuario.setRole(usuarioDTO.role());

        Usuario usuarioAtualizado = usuarioService.atualizar(usuario);
        return ResponseEntity.ok(new UsuarioExibicaoDTO(usuarioAtualizado));
    }
}

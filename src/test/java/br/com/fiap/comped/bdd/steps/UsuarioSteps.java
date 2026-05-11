package br.com.fiap.comped.bdd.steps;

import br.com.fiap.comped.bdd.service.UsuarioService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class UsuarioSteps {

    UsuarioService usuarioService = new UsuarioService();

    long idUsuarioCriado;
    long idBusca;

    @Dado("que eu tenha os seguintes dados do usuário:")
    public void queEuTenhaOsSeguintesDadosDoUsuario(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            usuarioService.setFieldsUsuario(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }
    }

    // POST /auth/register
    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de usuários")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeUsuarios(String endPoint) {
        usuarioService.createUsuario(endPoint);
    }

    // GET /api/usuarios
    @Quando("eu enviar a requisição para o endpoint {string} de listagem de usuários")
    public void euEnviarARequisicaoParaOEndpointDeListagemDeUsuarios(String endPoint) {
        usuarioService.getAllUsuarios(endPoint);
    }

    // Cenário de ID inexistente
    @Dado("que não existe usuário com ID {int}")
    public void queNaoExisteUsuarioComID(int id) {
        idBusca = id;
    }

    // GET /api/usuarios/{id}
    @Quando("eu enviar a requisição de busca para o endpoint {string}")
    public void euEnviarARequisicaoDeBuscaParaOEndpoint(String endPoint) {
        usuarioService.getUsuarioById("/api/usuarios", idBusca);
    }

    // Cria um usuário real para PUT e DELETE
    @Dado("que existe um usuário cadastrado no sistema")
    public void queExisteUmUsuarioCadastradoNoSistema() {

        String emailUnico =
                "teste_" + System.currentTimeMillis() + "@email.com";

        usuarioService.setFieldsUsuario(
                "nomeUsuario",
                "Usuário Teste"
        );

        usuarioService.setFieldsUsuario(
                "emailUsuario",
                emailUnico
        );

        usuarioService.setFieldsUsuario(
                "senhaUsuario",
                "Senha@123"
        );

        usuarioService.setFieldsUsuario(
                "role",
                "ADMIN"
        );

        usuarioService.createUsuario("/auth/register");

        idUsuarioCriado = usuarioService.response
                .jsonPath()
                .getLong("idUsuario");
    }

    @Dado("que existe pelo menos um usuário cadastrado no sistema")
    public void queExistePeloMenosUmUsuarioCadastradoNoSistema() {

        String emailUnico =
                "base_" + System.currentTimeMillis() + "@email.com";

        usuarioService.setFieldsUsuario(
                "nomeUsuario",
                "Usuário Base"
        );

        usuarioService.setFieldsUsuario(
                "emailUsuario",
                emailUnico
        );

        usuarioService.setFieldsUsuario(
                "senhaUsuario",
                "Senha@123"
        );

        usuarioService.setFieldsUsuario(
                "role",
                "ADMIN"
        );

        usuarioService.createUsuario("/auth/register");
    }

    // PUT /api/usuarios/{id}
    @E("que eu tenha os seguintes dados atualizados:")
    public void queEuTenhaOsSeguintesDadosAtualizados(
            List<Map<String, String>> rows
    ) {

        for (Map<String, String> columns : rows) {

            usuarioService.setFieldsUsuario(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }

        usuarioService.usuarioModel.setEmailUsuario(
                "atualizado_" +
                        System.currentTimeMillis() +
                        "@email.com"
        );
    }

    @Quando("eu enviar a requisição de atualização para o endpoint {string}")
    public void euEnviarARequisicaoDeAtualizacaoParaOEndpoint(String endPoint) {
        usuarioService.updateUsuario(
                "/api/usuarios",
                idUsuarioCriado
        );
    }

    // DELETE /api/usuarios/{id}
    @Quando("eu enviar a requisição de exclusão para o endpoint {string}")
    public void euEnviarARequisicaoDeExclusaoParaOEndpoint(String endPoint) {
        usuarioService.deleteUsuario(
                "/api/usuarios",
                idUsuarioCriado
        );
    }

    @Então("o status code da resposta deve ser {int}")
    public void oStatusCodeDaRespostaDeveSer(int statusCode) {

        Assert.assertEquals(
                statusCode,
                usuarioService.response.statusCode()
        );
    }
}
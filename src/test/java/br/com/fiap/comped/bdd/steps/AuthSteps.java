package br.com.fiap.comped.bdd.steps;

import br.com.fiap.comped.bdd.service.AuthService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class AuthSteps {

    AuthService authService = new AuthService();

    @Dado("que eu tenha os seguintes dados de autenticação:")
    public void queEuTenhaOsSeguintesDadosDeAutenticacao(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            authService.setFieldsAuth(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de login")
    public void euEnviarARequisicaoParaOEndpointDeLogin(String endPoint) {
        authService.login(endPoint);
    }

    @Quando("eu enviar a requisição para o endpoint {string} de registro")
    public void euEnviarARequisicaoParaOEndpointDeRegistro(String endPoint) {
        authService.register(endPoint);
    }

    @Então("o status code da resposta de auth deve ser {int}")
    public void oStatusCodeDaRespostaDeAuthDeveSer(int statusCode) {
        Assert.assertEquals(statusCode, authService.response.statusCode());
    }

    @Então("a resposta deve conter um token")
    public void aRespostaDeveConterUmToken() {
        String token = authService.response.jsonPath().getString("token");
        Assert.assertNotNull("Token não encontrado na resposta", token);
        Assert.assertFalse("Token está vazio", token.isBlank());
    }
}
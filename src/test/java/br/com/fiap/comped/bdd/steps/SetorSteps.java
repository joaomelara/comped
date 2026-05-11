package br.com.fiap.comped.bdd.steps;

import br.com.fiap.comped.bdd.service.SetorService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class SetorSteps {

    SetorService setorService = new SetorService();
    long idSetorCriado;

    @Dado("que eu tenha os seguintes dados do setor:")
    public void queEuTenhaOsSeguintesDadosDoSetor(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            setorService.setFieldsSetor(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }
    }

    // POST /setores
    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de setores")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeSetores(String endPoint) {
        setorService.createSetor(endPoint);
    }

    // GET /setores
    @Quando("eu enviar a requisição para o endpoint {string} de listagem de setores")
    public void euEnviarARequisicaoParaOEndpointDeListagemDeSetores(String endPoint) {
        setorService.getAllSetores(endPoint);
    }

    // GET /setores com paginação
    @Quando("eu enviar a requisição para o endpoint {string} de listagem de setores com paginação")
    public void euEnviarARequisicaoParaOEndpointDeListagemDeSetoresComPaginacao(String endPoint) {
        setorService.getAllSetores(endPoint);
    }

    // GET /setores com filtro
    @Quando("eu enviar a requisição para o endpoint {string} de listagem de setores com filtro")
    public void euEnviarARequisicaoParaOEndpointDeListagemDeSetoresComFiltro(String endPoint) {
        setorService.getAllSetores(endPoint);
    }

    // Cria um setor real para testes
    @Dado("que existe um setor cadastrado no sistema")
    public void queExisteUmSetorCadastradoNoSistema() {

        String nomeUnico = "Setor_" + System.currentTimeMillis();

        setorService.setFieldsSetor(
                "nomeSetor",
                nomeUnico
        );

        setorService.createSetor("/setores");

        idSetorCriado = setorService.response
                .jsonPath()
                .getLong("idSetor");
    }

    @Dado("que existe pelo menos um setor cadastrado no sistema")
    public void queExistePeloMenosUmSetorCadastradoNoSistema() {

        String nomeUnico = "Base_" + System.currentTimeMillis();

        setorService.setFieldsSetor(
                "nomeSetor",
                nomeUnico
        );

        setorService.createSetor("/setores");
    }

    @Dado("que existe um setor com nome {string} cadastrado no sistema")
    public void queExisteUmSetorComNomeCadastradoNoSistema(String nomeSetor) {

        String nomeUnico = nomeSetor + "_" + System.currentTimeMillis();

        setorService.setFieldsSetor(
                "nomeSetor",
                nomeUnico
        );

        setorService.createSetor("/setores");
    }

    @Então("o status code da resposta deve ser {int}")
    public void oStatusCodeDaRespostaDeveSer(int statusCode) {

        Assert.assertEquals(
                statusCode,
                setorService.response.statusCode()
        );
    }
}


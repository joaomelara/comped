package br.com.fiap.comped.bdd.steps;

import br.com.fiap.comped.bdd.service.SetorService;
import com.networknt.schema.ValidationMessage;
import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.json.JSONException;
import org.junit.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetorSteps {

    SetorService setorService = new SetorService();

    @Dado("que eu tenha os seguintes dados do setor:")
    public void queEuTenhaOsSeguintesDadosDoSetor(
            List<Map<String, String>> rows
    ) {

        for (Map<String, String> columns : rows) {

            setorService.setFieldsSetor(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }
    }

    // POST /setores
    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de setores")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeSetores(
            String endPoint
    ) {

        setorService.createSetor(endPoint);
    }

    // GET /setores
    @Quando("eu enviar a requisição para o endpoint {string} de listagem de setores")
    public void euEnviarARequisicaoParaOEndpointDeListagemDeSetores(
            String endPoint
    ) {

        setorService.getAllSetores(endPoint);
    }

    @Dado("que existe pelo menos um setor cadastrado no sistema")
    public void queExistePeloMenosUmSetorCadastradoNoSistema() {

        setorService.setFieldsSetor(
                "nomeSetor",
                "TI_" + System.currentTimeMillis()
        );

        setorService.createSetor("/setores");
    }

    @Então("o status code da resposta de setor deve ser {int}")
    public void oStatusCodeDaRespostaDeSetorDeveSer(
            int statusCode
    ) {

        Assert.assertEquals(
                statusCode,
                setorService.response.statusCode()
        );
    }

    @E("que o arquivo de contrato esperado é o {string}")
    public void queOArquivoDeContratoEsperadoÉO(String contract) throws IOException {
        setorService.setContract(contract);
    }

    @Então("a resposta da requisição deve estar em conformidade com o contrato selecionado")
    public void aRespostaDaRequisiçãoDeveEstarEmConformidadeComOContratoSelecionado() throws JSONException, IOException {
        Set<ValidationMessage> validateResponse = setorService.validateResponseAgainstSchema();
        Assert.assertTrue("O contrato está inválido. Erros encontrados: " + validateResponse, validateResponse.isEmpty());
    }
}
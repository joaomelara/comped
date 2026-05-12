package br.com.fiap.comped.bdd.steps;

import br.com.fiap.comped.bdd.service.ConsumoService;
import com.networknt.schema.ValidationMessage;
import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.json.JSONException;
import org.junit.Assert;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConsumoSteps {

    ConsumoService consumoService = new ConsumoService();

    private long equipIdCriado; // ← guardar o equipId usado na criação

    @Dado("que existe pelo menos um consumo cadastrado no sistema")
    public void queExistePeloMenosUmConsumoCadastradoNoSistema() {
        equipIdCriado = 3L; // ← guardar o id

        consumoService.setFieldsConsumo("equipId",     String.valueOf(equipIdCriado));
        consumoService.setFieldsConsumo("dataConsumo", Instant.now().toString());
        consumoService.setFieldsConsumo("kwhConsumo",  "12.5");
        consumoService.createConsumo("/consumos");
    }

    @Quando("eu enviar a requisição para o endpoint {string} de listagem de consumos")
    public void euEnviarARequisicaoParaOEndpointDeListagemDeConsumos(String endPoint) {
        consumoService.getConsumosPorEquip(endPoint, equipIdCriado); // ← passar equipId
    }

    @Então("o status code da resposta de consumo deve ser {int}")
    public void oStatusCodeDaRespostaDeConsumoDeveSer(int statusCode) {
        Assert.assertEquals(
                statusCode,
                consumoService.response.statusCode()
        );
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de consumos")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeConsumos(String endPoint) {
        consumoService.createConsumo(endPoint);
    }

    @Dado("que eu tenha os seguintes dados do consumo:")
    public void queEuTenhaOsSeguintesDadosDoConsumo(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            consumoService.setFieldsConsumo(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }
    }

    @E("que o arquivo de contrato do consumo esperado é o {string}")
    public void queOArquivoDeContratoDoConsumoEsperadoÉO(String contract) throws IOException {
        consumoService.setContract(contract);
    }

    @Então("a resposta da requisição deve estar em conformidade com o contrato do consumo selecionado")
    public void aRespostaDaRequisiçãoDeveEstarEmConformidadeComOContratoDoConsumoSelecionado() throws JSONException, IOException {
        Set<ValidationMessage> validateResponse = consumoService.validateResponseAgainstSchema();
        Assert.assertTrue("O contrato está inválido. Erros encontrados: " + validateResponse, validateResponse.isEmpty());
    }
}
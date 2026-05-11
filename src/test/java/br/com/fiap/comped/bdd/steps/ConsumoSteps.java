package br.com.fiap.comped.bdd.steps;

import br.com.fiap.comped.bdd.service.ConsumoService;
import br.com.fiap.comped.bdd.service.UsuarioService;
import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ConsumoSteps {

    ConsumoService consumoService = new ConsumoService();

    @Dado("que existe pelo menos um consumo cadastrado no sistema")
    public void queExistePeloMenosUmConsumoCadastradoNoSistema() {
        consumoService.setFieldsConsumo(
                    "equipamentoId",
                    "1"
        );
        consumoService.setFieldsConsumo(
                "dataConsumo",
                Instant.now().toString()
        );
        consumoService.setFieldsConsumo(
                "kwhConsumo",
                "12.5"
        );
        consumoService.createConsumo("/consumos");
    }

    @Quando("eu enviar a requisição para o endpoint {string} de listagem de consumos")
    public void euEnviarARequisicaoParaOEndpointDeListagemDeConsumos(String endPoint) {
        consumoService.getAllConsumos(endPoint);
    }

    @Então("o status code da resposta de consumo deve ser {int}")
    public void oStatusCodeDaRespostaDeConsumoDeveSer(
            int statusCode
    ) {

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
}

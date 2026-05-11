package br.com.fiap.comped.bdd.steps;

import br.com.fiap.comped.bdd.service.EquipamentoService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class EquipamentoSteps {

    EquipamentoService equipamentoService =
            new EquipamentoService();

    Long idEquipamentoCriado = 1L;

    @Dado("que eu tenha os seguintes dados do equipamento:")
    public void queEuTenhaOsSeguintesDadosDoEquipamento(
            List<Map<String, String>> rows
    ) {

        for (Map<String, String> columns : rows) {

            equipamentoService.setFieldsEquipamento(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }
    }

    // POST
    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de equipamentos")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeEquipamentos(
            String endPoint
    ) {

        equipamentoService.createEquipamento(endPoint);
    }

    // GET
    @Quando("eu enviar a requisição para o endpoint {string} de listagem de equipamentos")
    public void euEnviarARequisicaoParaOEndpointDeListagemDeEquipamentos(
            String endPoint
    ) {

        equipamentoService.getAllEquipamentos(endPoint);
    }

    @Dado("que existe pelo menos um equipamento cadastrado no sistema")
    public void queExistePeloMenosUmEquipamentoCadastradoNoSistema() {

        equipamentoService.setFieldsEquipamento(
                "nomeEquipamento",
                "Ar Condicionado"
        );

        equipamentoService.setFieldsEquipamento(
                "consumoKwh",
                "12.5"
        );

        equipamentoService.setFieldsEquipamento(
                "ativo",
                "true"
        );

        equipamentoService.setFieldsEquipamento(
                "setorId",
                "1"
        );

        equipamentoService.createEquipamento(
                "/equipamentos"
        );

        idEquipamentoCriado = equipamentoService.response
                .jsonPath()
                .getLong("id");
    }

    // PUT
    @E("que eu tenha os seguintes dados atualizados do equipamento:")
    public void queEuTenhaOsSeguintesDadosAtualizadosDoEquipamento(
            List<Map<String, String>> rows
    ) {

        for (Map<String, String> columns : rows) {

            equipamentoService.setFieldsEquipamento(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }
    }

    @Quando("eu enviar a requisição de atualização para o endpoint {string} de equipamentos")
    public void euEnviarARequisicaoDeAtualizacaoParaOEndpointDeEquipamentos(
            String endPoint
    ) {

        equipamentoService.updateEquipamento(
                "/equipamentos",
                idEquipamentoCriado
        );
    }

    @Então("o status code da resposta de equipamento deve ser {int}")
    public void oStatusCodeDaRespostaDeEquipamentoDeveSer(
            int statusCode
    ) {

        Assert.assertEquals(
                statusCode,
                equipamentoService.response.statusCode()
        );
    }
}
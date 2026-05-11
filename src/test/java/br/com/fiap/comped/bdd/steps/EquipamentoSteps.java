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

    EquipamentoService equipamentoService = new EquipamentoService();

    long idEquipamentoCriado;

    @Dado("que eu tenha os seguintes dados do equipamento:")
    public void queEuTenhaOsSeguintesDadosDoEquipamento(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            equipamentoService.setFieldsEquipamento(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }
    }


    @Dado("que existe um equipamento cadastrado no sistema")
    public void queExisteUmEquipamentoCadastradoNoSistema() {
        equipamentoService.setFieldsEquipamento("nomeEquipamento", "Equipamento Teste BDD");
        equipamentoService.setFieldsEquipamento("setorId",         "1");
        equipamentoService.setFieldsEquipamento("dataInstalacao",  "2024-01-15");
        equipamentoService.setFieldsEquipamento("limiteKwh",       "100.0");
        equipamentoService.createEquipamento("/equipamentos");

        idEquipamentoCriado = equipamentoService.response
                .jsonPath()
                .getLong("idEquipamento");
    }

    @Dado("que existe pelo menos um equipamento cadastrado no sistema")
    public void queExistePeloMenosUmEquipamentoCadastradoNoSistema() {
        equipamentoService.setFieldsEquipamento("nomeEquipamento", "Equipamento Base BDD");
        equipamentoService.setFieldsEquipamento("setorId",         "1");
        equipamentoService.setFieldsEquipamento("dataInstalacao",  "2024-01-15");
        equipamentoService.setFieldsEquipamento("limiteKwh",       "50.0");
        equipamentoService.createEquipamento("/equipamentos");
    }


    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de equipamentos")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeEquipamentos(String endPoint) {
        equipamentoService.createEquipamento(endPoint);
    }

    @Quando("eu enviar a requisição para o endpoint {string} de listagem de equipamentos")
    public void euEnviarARequisicaoParaOEndpointDeListagemDeEquipamentos(String endPoint) {
        equipamentoService.getAllEquipamentos(endPoint);
    }


    @E("que eu tenha os seguintes dados atualizados do equipamento:")
    public void queEuTenhaOsSeguintesDadosAtualizadosDoEquipamento(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            equipamentoService.setFieldsEquipamento(
                    columns.get("campo"),
                    columns.get("valor")
            );
        }
    }

    @Quando("eu enviar a requisição de atualização para o endpoint {string} de equipamentos")
    public void euEnviarARequisicaoDeAtualizacaoParaOEndpointDeEquipamentos(String endPoint) {
        equipamentoService.updateEquipamento("/equipamentos", idEquipamentoCriado);
    }


    @Quando("eu enviar a requisição de atualização para o endpoint {string} de equipamento inexistente")
    public void euEnviarARequisicaoDeAtualizacaoParaEquipamentoInexistente(String endPoint) {
        equipamentoService.updateEquipamentoInexistente("/equipamentos", 999999L);
    }


    @Então("o status code da resposta de equipamentos deve ser {int}")
    public void oStatusCodeDaRespostaDeEquipamentosDeveSer(int statusCode) {
        Assert.assertEquals(statusCode, equipamentoService.response.statusCode());
    }
}
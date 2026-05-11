package br.com.fiap.comped.bdd.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import br.com.fiap.comped.bdd.model.EquipamentoModel;

import static io.restassured.RestAssured.given;

public class EquipamentoService {

    public EquipamentoModel equipamentoModel = new EquipamentoModel();

    public final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public Response response;

    String baseUrl    = "http://localhost:8080";
    String emailLogin = "ZZ@email.com";
    String senhaLogin = "123456";

    private String tokenCache = null;

    private String obterToken() {
        if (tokenCache != null) return tokenCache;

        String body = String.format(
                "{\"emailUsuario\":\"%s\",\"senhaUsuario\":\"%s\"}",
                emailLogin, senhaLogin
        );

        tokenCache = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(baseUrl + "/auth/login")
                .then()
                .statusCode(200) // falha rápida se login não funcionar
                .extract()
                .jsonPath()
                .getString("token");

        return tokenCache;
    }

    private RequestSpecification request() {
        return given()
                .header("Authorization", "Bearer " + obterToken())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    public void setFieldsEquipamento(String field, String value) {
        switch (field) {
            case "nomeEquipamento" -> equipamentoModel.setNomeEquipamento(value);
            case "setorId"         -> equipamentoModel.setSetorId(Long.parseLong(value));
            case "dataInstalacao"  -> equipamentoModel.setDataInstalacao(value);
            case "limiteKwh"       -> equipamentoModel.setLimiteKwh(Double.parseDouble(value));
            case "ativo"           -> equipamentoModel.setAtivo(Boolean.parseBoolean(value));
            default -> throw new IllegalStateException("Campo inesperado: " + field);
        }
    }

    public void createEquipamento(String endPoint) {
        response = request()
                .body(gson.toJson(equipamentoModel))
                .when().post(baseUrl + endPoint)
                .then().extract().response();
    }

    public void getAllEquipamentos(String endPoint) {
        response = request()
                .when().get(baseUrl + endPoint)
                .then().extract().response();
    }

    public void updateEquipamento(String endPoint, long id) {
        response = request()
                .body(gson.toJson(equipamentoModel))
                .when().put(baseUrl + endPoint + "/" + id)
                .then().extract().response();
    }

    public void updateEquipamentoInexistente(String endPoint, long id) {
        String bodyMinimo = "{\"nomeEquipamento\":\"Teste Inexistente\",\"limiteKwh\":1.0,\"ativo\":true}";
        response = request()
                .body(bodyMinimo)
                .when().put(baseUrl + endPoint + "/" + id)
                .then().extract().response();
    }
}
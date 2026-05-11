package br.com.fiap.comped.bdd.service;

import br.com.fiap.comped.bdd.model.SetorModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class SetorService {

    public SetorModel setorModel = new SetorModel();

    public final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public Response response;

    String baseUrl    = "http://localhost:8080";
    String emailLogin = "ZZ@email.com";
    String senhaLogin = "123456";

    private String obterToken() {
        String body = String.format(
                "{\"emailUsuario\":\"%s\",\"senhaUsuario\":\"%s\"}",
                emailLogin, senhaLogin
        );
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(baseUrl + "/auth/login")
                .then()
                .extract()
                .jsonPath()
                .getString("token");
    }

    private RequestSpecification request() {
        return given()
                .header("Authorization", "Bearer " + obterToken())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    public void setFieldsSetor(String field, String value) {
        switch (field) {
            case "nomeSetor" -> setorModel.setNomeSetor(value);
            default -> throw new IllegalStateException("Campo inesperado: " + field);
        }
    }

    public void createSetor(String endPoint) {
        String nomeAtual = setorModel.getNomeSetor();
        if (nomeAtual != null && !nomeAtual.isBlank()) {
            setorModel.setNomeSetor(nomeAtual + "_" + System.currentTimeMillis());
        }

        response = request()
                .body(gson.toJson(setorModel))
                .when()
                .post(baseUrl + endPoint)
                .then()
                .extract()
                .response();
    }

    public void getAllSetores(String endPoint) {
        response = request()
                .when()
                .get(baseUrl + endPoint)
                .then()
                .extract()
                .response();
    }
}
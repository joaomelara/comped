package br.com.fiap.comped.bdd.service;

import br.com.fiap.comped.bdd.model.AuthModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthService {

    public AuthModel authModel = new AuthModel();

    public final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public Response response;

    String baseUrl = "http://localhost:8080";

    public void setFieldsAuth(String field, String value) {
        switch (field) {
            case "emailUsuario" -> authModel.setEmailUsuario(value);
            case "senhaUsuario" -> authModel.setSenhaUsuario(value);
            case "nomeUsuario"  -> authModel.setNomeUsuario(value);
            case "role"         -> authModel.setRole(value);
            default -> throw new IllegalStateException("Campo inesperado: " + field);
        }
    }

    // POST /auth/login
    public void login(String endPoint) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gson.toJson(authModel))
                .when()
                .post(baseUrl + endPoint)
                .then()
                .extract()
                .response();

        System.out.println("[BDD] POST " + endPoint + " → " + response.statusCode() + " | " + response.asString());
    }

    public void register(String endPoint) {
        String emailAtual = authModel.getEmailUsuario();
        if (emailAtual != null && emailAtual.contains("@")) {
            authModel.setEmailUsuario("user_" + System.currentTimeMillis() + "@email.com");
        }

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gson.toJson(authModel))
                .when()
                .post(baseUrl + endPoint)
                .then()
                .extract()
                .response();

        System.out.println("[BDD] POST " + endPoint + " → " + response.statusCode() + " | " + response.asString());
    }
}


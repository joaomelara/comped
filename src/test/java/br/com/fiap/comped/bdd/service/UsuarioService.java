package br.com.fiap.comped.bdd.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import br.com.fiap.comped.bdd.model.UsuarioModel;

import static io.restassured.RestAssured.given;

public class UsuarioService {

    final UsuarioModel usuarioModel = new UsuarioModel();
    public final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    public Response response;
    String baseUrl = "http://localhost:8080";

    String emailLogin = "admin@email.com";
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

    public void setFieldsUsuario(String field, String value) {
        switch (field) {
            case "nomeUsuario"  -> usuarioModel.setNomeUsuario(value);
            case "emailUsuario" -> usuarioModel.setEmailUsuario(value);
            case "senhaUsuario" -> usuarioModel.setSenhaUsuario(value);
            case "role"         -> usuarioModel.setRole(value);
            default -> throw new IllegalStateException("Unexpected field: " + field);
        }
    }

    public void createUsuario(String endPoint) {
        response = request()
                .body(gson.toJson(usuarioModel))
                .when().post(baseUrl + endPoint)
                .then().extract().response();
    }

    public void getAllUsuarios(String endPoint) {
        response = request()
                .when().get(baseUrl + endPoint)
                .then().extract().response();
    }

    public void getUsuarioById(String endPoint, int id) {
        response = request()
                .when().get(baseUrl + endPoint + "/" + id)
                .then().extract().response();
    }

    public void updateUsuario(String endPoint, int id) {
        response = request()
                .body(gson.toJson(usuarioModel))
                .when().put(baseUrl + endPoint + "/" + id)
                .then().extract().response();
    }

    public void deleteUsuario(String endPoint, int id) {
        response = request()
                .when().delete(baseUrl + endPoint + "/" + id)
                .then().extract().response();
    }
}
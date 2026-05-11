package br.com.fiap.comped.bdd.service;

import br.com.fiap.comped.bdd.model.ConsumoModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.time.Instant;

import static io.restassured.RestAssured.given;

public class ConsumoService {

    public ConsumoModel consumoModel = new ConsumoModel();

    public final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public Response response;

    String baseUrl    = "http://localhost:8080";
    String emailLogin = "ZZ@email.com";
    String senhaLogin = "123456";

    private static String tokenCache = null;

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
                .statusCode(200) // ← falha rápida se credenciais erradas
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

    public void setFieldsConsumo(String field, String value) {
        switch (field) {
            case "equipId" ->
                    consumoModel.setEquipId(Long.parseLong(value));

            case "dataConsumo" -> {
                if (value == null || value.isBlank()) {
                    consumoModel.setDataConsumo(null); // testa @NotNull → 400
                } else if (value.equalsIgnoreCase("now")) {
                    consumoModel.setDataConsumo(Instant.now().toString()); // dinâmico
                } else {
                    consumoModel.setDataConsumo(value); // valor literal do .feature
                }
            }

            case "kwhConsumo" ->
                    consumoModel.setKwhConsumo(Double.parseDouble(value));

            default -> throw new IllegalStateException("Campo inesperado: " + field);
        }
    }

    // POST /consumos
    public void createConsumo(String endPoint) {
        response = request()
                .body(gson.toJson(consumoModel))
                .when()
                .post(baseUrl + endPoint)
                .then()
                .extract()
                .response();

        System.out.println("[BDD] POST " + endPoint + " → " + response.statusCode() + " | " + response.asString());
    }

    // GET /consumos
    public void getAllConsumos(String endPoint) {
        response = request()
                .when()
                .get(baseUrl + endPoint)
                .then()
                .extract()
                .response();

        System.out.println("[BDD] GET " + endPoint + " → " + response.statusCode());
    }

    // Adicionar método para GET com equipId
    public void getConsumosPorEquip(String endPoint, Long equipId) {
        response = request()
                .queryParam("equipId", equipId)
                .when()
                .get(baseUrl + endPoint)
                .then()
                .extract()
                .response();

        System.out.println("[BDD] GET " + endPoint + "?equipId=" + equipId
                + " → " + response.statusCode());
    }
}
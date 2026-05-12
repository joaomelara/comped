package br.com.fiap.comped.bdd.service;

import br.com.fiap.comped.bdd.model.ConsumoModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class ConsumoService {

    String schemasPath = "src/test/resources/schemas/";
    JSONObject jsonSchema;
    private final ObjectMapper mapper = new ObjectMapper();

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

    private JSONObject loadJsonFromFile(String filePath) throws IOException {
        try {
            String jsonContent = Files.readString(Paths.get(filePath));
            return new JSONObject(jsonContent);
        } catch (JSONException e) {
            throw new RuntimeException("Arquivo de schema JSON inválido: " + filePath, e);
        }
    }

    public void setContract(String contract) throws IOException {
        switch (contract) {
            case "Cadastro bem-sucedido de consumo" -> jsonSchema = loadJsonFromFile(schemasPath + "cadastro-bem-sucedido-consumo.json");
            default -> throw new IllegalStateException("Contrato inesperado: " + contract);
        }
    }

    public Set<ValidationMessage> validateResponseAgainstSchema() throws IOException, JSONException {
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema schema = schemaFactory.getSchema(jsonSchema.toString());
        JsonNode jsonResponseNode = mapper.readTree(jsonResponse.toString());
        Set<ValidationMessage> schemaValidationErrors = schema.validate(jsonResponseNode);
        return schemaValidationErrors;
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
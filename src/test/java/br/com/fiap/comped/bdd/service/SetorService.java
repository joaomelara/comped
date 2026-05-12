package br.com.fiap.comped.bdd.service;

import br.com.fiap.comped.bdd.model.SetorModel;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class SetorService {

    String schemasPath = "src/test/resources/schemas/";
    JSONObject jsonSchema;
    private final ObjectMapper mapper = new ObjectMapper();

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
            case "Cadastro bem-sucedido de setor" -> jsonSchema = loadJsonFromFile(schemasPath + "cadastro-bem-sucedido-setor.json");
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

    public void setFieldsSetor(String field, String value) {

        switch (field) {

            case "nomeSetor" -> setorModel.setNomeSetor(value);
            default -> throw new IllegalStateException("Campo inesperado: " + field);
        }
    }

    // POST
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

    // GET ALL
    public void getAllSetores(String endPoint) {

        response = request()
                .when()
                .get(baseUrl + endPoint)
                .then()
                .extract()
                .response();
    }
}
package br.com.fiap.comped.bdd.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import br.com.fiap.comped.bdd.model.SetorModel;

import static io.restassured.RestAssured.given;

public class SetorService {

    public SetorModel setorModel = new SetorModel();
    public final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    public Response response;
    String baseUrl = "http://localhost:8080";

    private RequestSpecification request() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    public void setFieldsSetor(String field, String value) {
        switch (field) {
            case "nomeSetor" -> setorModel.setNomeSetor(value);
            default -> throw new IllegalStateException("Unexpected field: " + field);
        }
    }

    public void createSetor(String endPoint) {
        response = request()
                .body(gson.toJson(setorModel))
                .when().post(baseUrl + endPoint)
                .then().extract().response();
    }

    public void getAllSetores(String endPoint) {
        response = request()
                .when().get(baseUrl + endPoint)
                .then().extract().response();
    }

    public void getSetorById(String endPoint, long id) {
        response = request()
                .when().get(baseUrl + endPoint + "/" + id)
                .then().extract().response();
    }
}


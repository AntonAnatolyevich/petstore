package ru.akhramenko.yandex.petstore.oldtests;

import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class BaseTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    protected String jsonBody(String... keyValues) {
        StringBuilder json = new StringBuilder("{");
        for (int i = 0; i < keyValues.length; i += 2) {
            if (i > 0) json.append(",");
            json.append("\"").append(keyValues[i]).append("\":");
            if (keyValues[i + 1].matches("\\d+")) {
                json.append(keyValues[i + 1]);
            } else {
                json.append("\"").append(keyValues[i + 1]).append("\"");
            }
        }
        json.append("}");
        return json.toString();
    }

    protected long createTestPet(String name, String status) {
        String petJson = jsonBody("name", name, "status", status);
        return given().contentType("application/json").body(petJson)
                .when().post("/pet")
                .then().extract().path("id");
    }

    protected int createTestOrder(int petId, int quantity) {
        String orderJson = jsonBody("petId", String.valueOf(petId), "quantity", String.valueOf(quantity));
        return given().contentType("application/json").body(orderJson)
                .when().post("/store/order")
                .then().extract().path("id");
    }

    protected String createTestUser() {
        String username = "user_" + System.currentTimeMillis();
        String userJson = jsonBody("username", username, "email", "test@test.com");
        given().contentType("application/json").body(userJson).when().post("/user");
        return username;
    }

    protected void cleanupPet(long petId) {
        try {
            given().pathParam("petId", petId).when().delete("/pet/{petId}");
        } catch (Exception e) {}
    }

    protected void cleanupOrder(int orderId) {
        try {
            given().pathParam("orderId", orderId).when().delete("/store/order/{orderId}");
        } catch (Exception e) {}
    }

    protected void cleanupUser(String username) {
        try {
            given().pathParam("username", username).when().delete("/user/{username}");
        } catch (Exception e) {}
    }
}

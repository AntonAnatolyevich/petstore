package ru.akhramenko.yandex.petstore.newest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class NewNewBaseTest {

    protected static final long TEST_PET_ID = 1000L;
    protected static final int TEST_ORDER_ID = 100;
    protected static final String TEST_USERNAME = "testuser";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    // ==================== PET METHODS ====================

    protected void createTestPet() {
        String petJson = "{\"id\":1000,\"name\":\"TestPet\",\"status\":\"available\",\"photoUrls\":[\"http://test.com/photo.jpg\"]}";
        given().contentType("application/json").body(petJson).post("/pet");
    }

    protected Response getPetWithRetry() {
        int attempts = 0;
        while (attempts < 30) {
            Response response = given().pathParam("petId", TEST_PET_ID).get("/pet/{petId}");
            if (response.getStatusCode() == 200) return response;
            attempts++;
            try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
        }
        return given().pathParam("petId", TEST_PET_ID).get("/pet/{petId}");
    }

    protected void deleteTestPet() {
        given().pathParam("petId", TEST_PET_ID).delete("/pet/{petId}");
    }

    protected void ensurePetExists() {
        try { deleteTestPet(); } catch (Exception e) {}
        createTestPet();
        getPetWithRetry();
    }

    protected void ensurePetNotExists() {
        try { deleteTestPet(); } catch (Exception e) {}
        int attempts = 0;
        while (attempts < 30) {
            Response response = given().pathParam("petId", TEST_PET_ID).get("/pet/{petId}");
            if (response.getStatusCode() == 404) break;
            attempts++;
            try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
        }
    }

    // ==================== STORE METHODS ====================

    protected void createTestOrder() {
        String orderJson = "{\"id\":100,\"petId\":1000,\"quantity\":1,\"status\":\"placed\"}";
        given().contentType("application/json").body(orderJson).post("/store/order");
    }

    protected Response getOrderWithRetry() {
        int attempts = 0;
        while (attempts < 30) {
            Response response = given().pathParam("orderId", TEST_ORDER_ID).get("/store/order/{orderId}");
            if (response.getStatusCode() == 200) return response;
            attempts++;
            try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
        }
        return given().pathParam("orderId", TEST_ORDER_ID).get("/store/order/{orderId}");
    }

    protected void deleteTestOrder() {
        given().pathParam("orderId", TEST_ORDER_ID).delete("/store/order/{orderId}");
    }

    // ==================== USER METHODS ====================

    protected void createTestUser() {
        String userJson = "{\"username\":\"testuser\",\"email\":\"test@test.com\"}";
        given().contentType("application/json").body(userJson).post("/user");
    }

    protected Response getUserWithRetry() {
        int attempts = 0;
        while (attempts < 30) {
            Response response = given().pathParam("username", TEST_USERNAME).get("/user/{username}");
            if (response.getStatusCode() == 200) return response;
            attempts++;
            try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
        }
        return given().pathParam("username", TEST_USERNAME).get("/user/{username}");
    }

    protected void deleteTestUser() {
        given().pathParam("username", TEST_USERNAME).delete("/user/{username}");
    }

    protected void ensureUserExists() {
        try { deleteTestUser(); } catch (Exception e) {}
        createTestUser();
        getUserWithRetry();
    }

    protected void ensureUserNotExists() {
        try {
            deleteTestUser();
        } catch (Exception e) {
        }
        int attempts = 0;
        while (attempts < 30) {
            Response response = given().pathParam("username", TEST_USERNAME).get("/user/{username}");
            if (response.getStatusCode() == 404) break;
            attempts++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

package ru.akhramenko.yandex.petstore;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class StoreTest extends BaseTest {

    @Test
    void getInventory() {
        when().get("/store/inventory").then().statusCode(200)
                .body("available", notNullValue());
    }

    @Test
    void placeOrder() {
        String orderJson = jsonBody("petId", "123", "quantity", "1", "status", "placed");
        given().contentType("application/json").body(orderJson)
                .when().post("/store/order").then().statusCode(200)
                .body("status", equalTo("placed"));
    }

    @Test
    void getOrderById() {
        int orderId = createTestOrder(123, 1);
        given().pathParam("orderId", orderId)
                .when().get("/store/order/{orderId}").then().statusCode(200);
    }

    @Test
    void getOrderByIdNotFound() {
        given().pathParam("orderId", 999999)
                .when().get("/store/order/{orderId}").then().statusCode(404);
    }

    @Test
    void deleteOrder() {
        int orderId = createTestOrder(123, 1);
        given().pathParam("orderId", orderId)
                .when().delete("/store/order/{orderId}").then().statusCode(200);
    }

    @Test
    void deleteOrderNotFound() {
        given().pathParam("orderId", 999999)
                .when().delete("/store/order/{orderId}").then().statusCode(404);
    }
}

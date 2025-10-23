package ru.akhramenko.yandex.petstore;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;


import org.junit.jupiter.api.Test;

public class UserTest extends BaseTest{

    @Test
    void createUser() {
        String userJson = jsonBody("username", "user1", "email", "test@test.com");
        given().contentType("application/json").body(userJson)
                .when().post("/user").then().statusCode(200);
    }

    @Test
    void createUsersWithArray() {
        String usersJson = "[{\"username\":\"user1\"}, {\"username\":\"user2\"}]";
        given().contentType("application/json").body(usersJson)
                .when().post("/user/createWithArray").then().statusCode(200);
    }

    @Test
    void createUsersWithList() {
        String usersJson = "[{\"username\":\"user1\"}, {\"username\":\"user2\"}]";
        given().contentType("application/json").body(usersJson)
                .when().post("/user/createWithList").then().statusCode(200);
    }

    @Test
    void getUser() {
        String username = createTestUser();
        given().pathParam("username", username)
                .when().get("/user/{username}").then().statusCode(200);
    }

    @Test
    void getUserNotFound() {
        given().pathParam("username", "nonexistent")
                .when().get("/user/{username}").then().statusCode(404);
    }

    @Test
    void updateUser() {
        String username = createTestUser();
        String updateJson = jsonBody("username", username, "email", "new@email.com");
        given().contentType("application/json").body(updateJson)
                .when().put("/user/" + username).then().statusCode(200);
    }

    @Test
    void deleteUser() {
        String username = createTestUser();
        given().pathParam("username", username)
                .when().delete("/user/{username}").then().statusCode(200);
    }

    @Test
    void userLogin() {
        String username = createTestUser();
        given().param("username", username).param("password", "password")
                .when().get("/user/login").then().statusCode(200);
    }

    @Test
    void userLogout() {
        when().get("/user/logout").then().statusCode(200);
    }
}

package ru.akhramenko.yandex.petstore.newest;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class NewNewStoreTest extends NewNewBaseTest{

    @Test
    void createUser_shouldCreateUser() {
        ensureUserNotExists();

        createTestUser();

        getUserWithRetry()
                .then().statusCode(200)
                .body("username", equalTo(TEST_USERNAME));

        deleteTestUser();
    }

    @Test
    void createUsersWithArray_shouldCreateUsers() {
        String usersJson = "[{\"username\":\"user1\"},{\"username\":\"user2\"}]";

        given().contentType("application/json").body(usersJson)
                .post("/user/createWithArray")
                .then().statusCode(200);
    }

    @Test
    void createUsersWithList_shouldCreateUsers() {
        String usersJson = "[{\"username\":\"user3\"},{\"username\":\"user4\"}]";

        given().contentType("application/json").body(usersJson)
                .post("/user/createWithList")
                .then().statusCode(200);
    }

    @Test
    void getUserByName_shouldReturnUser() {
        ensureUserExists();

        getUserWithRetry()
                .then().statusCode(200)
                .body("username", equalTo(TEST_USERNAME));

        deleteTestUser();
    }

    @Test
    void getUserByName_shouldReturn404_whenUserNotExists() {
        given().pathParam("username", "nonexistentuser")
                .get("/user/{username}")
                .then().statusCode(404);
    }

    @Test
    void updateUser_shouldUpdateUser() {
        ensureUserExists();

        String updateJson = "{\"username\":\"testuser\",\"email\":\"updated@test.com\"}";
        given().contentType("application/json").body(updateJson)
                .put("/user/" + TEST_USERNAME)
                .then().statusCode(200);

        deleteTestUser();
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        ensureUserExists();

        deleteTestUser();

        given().pathParam("username", TEST_USERNAME)
                .get("/user/{username}")
                .then().statusCode(404);
    }

    @Test
    void loginUser_shouldReturn200() {
        ensureUserExists();

        given().param("username", TEST_USERNAME)
                .param("password", "any")
                .get("/user/login")
                .then().statusCode(200);

        deleteTestUser();
    }

    @Test
    void logoutUser_shouldReturn200() {
        given().get("/user/logout")
                .then().statusCode(200);
    }

    @Test
    void deleteUser_shouldReturn404_whenUserNotExists() {
        given().pathParam("username", "nonexistentuser")
                .delete("/user/{username}")
                .then().statusCode(404);
    }
}

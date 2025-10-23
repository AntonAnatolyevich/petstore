package ru.akhramenko.yandex.petstore;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetTest extends BaseTest {


    @Test
    void createPetWithFullData() {
        String json = "{\"name\":\"Dog\",\"status\":\"available\",\"photoUrls\":[\"url1\"]}";

        long petId = given().contentType("application/json").body(json)
                .when().post("/pet").then().statusCode(200)
                .body("id", notNullValue()).body("name", equalTo("Dog"))
                .extract().path("id");

        cleanupPet(petId);
    }

    @Test
    void getPetById() {
        long petId = createTestPet("TestPet", "available");

        given().pathParam("petId", petId)
                .when().get("/pet/{petId}").then().statusCode(200)
                .body("id", equalTo((int) petId))
                .body("name", notNullValue());

        cleanupPet(petId);
    }

    @Test
    void getPetByIdNotFound() {
        long testId = System.currentTimeMillis();
        given().pathParam("petId", testId)
                .when().get("/pet/{petId}").then().statusCode(404);
    }

    @Test
    void updatePet() {
        long petId = createTestPet("OldName", "available");
        String updateJson = jsonBody("id", String.valueOf(petId), "name", "NewName", "status", "sold");
        given().contentType("application/json").body(updateJson)
                .when().put("/pet").then().statusCode(200)
                .body("name", equalTo("NewName"));
        cleanupPet(petId);
    }

    @Test
    void updatePetWithFormData() {
        long petId = createTestPet("Pet", "available");
        given().contentType("application/x-www-form-urlencoded")
                .formParam("name", "Updated").formParam("status", "sold")
                .when().post("/pet/" + petId).then().statusCode(200);
        cleanupPet(petId);
    }

    @Test
    void deletePet() {
        long petId = createTestPet("ToDelete", "available");
        given().pathParam("petId", petId)
                .when().delete("/pet/{petId}").then().statusCode(200);
    }

    @Test
    void findPetsByStatus() {
        long petId = createTestPet("StatusPet", "available");
        given().param("status", "available")
                .when().get("/pet/findByStatus").then().statusCode(200)
                .body("status", everyItem(equalTo("available")));
        cleanupPet(petId);
    }

    @Test
    void findPetsByTags() {
        given().param("tags", "friendly")
                .when().get("/pet/findByTags").then().statusCode(200);
    }

    @Test
    void uploadPetImage() {
        long petId = createTestPet("Pet", "available");
        given().contentType("multipart/form-data")
                .multiPart("file", "test.jpg", "content".getBytes())
                .when().post("/pet/" + petId + "/uploadImage").then().statusCode(200);
        cleanupPet(petId);
    }
}

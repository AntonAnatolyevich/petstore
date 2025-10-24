package ru.akhramenko.yandex.petstore.newest;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class NewNewPetTest extends NewNewBaseTest {

    @Test void addPet_shouldCreatePet() {
        ensurePetNotExists();
        createTestPet();
        getPetWithRetry().then().statusCode(200);
    }

    @Test void updatePet_shouldUpdatePet() {
        ensurePetExists();
        String updateJson = "{\"id\":1000,\"name\":\"UpdatedPet\",\"status\":\"sold\"}";
        given().contentType("application/json").body(updateJson).put("/pet");
    }

    @Test void findPetsByStatus_shouldReturnPets() {
        ensurePetExists();
        given().param("status", "available").get("/pet/findByStatus").then().statusCode(200);
    }

    @Test void findPetsByTags_shouldReturnPets() {
        ensurePetExists();
        given().param("tags", "tag1").get("/pet/findByTags").then().statusCode(200);
    }

    @Test void updatePetWithForm_shouldUpdatePet() {
        ensurePetExists();
        given().contentType("application/x-www-form-urlencoded")
                .formParam("name", "FormUpdated")
                .formParam("status", "pending")
                .post("/pet/" + TEST_PET_ID);
    }

    @Test void uploadImage_shouldUpload() {
        ensurePetExists();
        given().contentType("multipart/form-data")
                .multiPart("file", "test.jpg", "content".getBytes())
                .post("/pet/" + TEST_PET_ID + "/uploadImage")
                .then().statusCode(200);
    }

    @Test void deletePet_shouldDeletePet() {
        ensurePetExists();
        deleteTestPet();
        given().pathParam("petId", TEST_PET_ID).get("/pet/{petId}").then().statusCode(404);
    }

    @Test void getPetById_shouldReturn404_whenPetNotExists() {
        ensurePetNotExists();
        given().pathParam("petId", TEST_PET_ID).get("/pet/{petId}").then().statusCode(404);
    }
}

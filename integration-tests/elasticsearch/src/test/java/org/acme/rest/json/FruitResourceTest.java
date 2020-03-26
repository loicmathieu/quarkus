package org.acme.rest.json;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;

@QuarkusTest
public class FruitResourceTest {
    private static final TypeRef<List<Fruit>> LIST_OF_FRUIT_TYPE_REF = new TypeRef<List<Fruit>>() {
    };

    @Test
    public void testHelloEndpoint() {
        // create a Fruit
        Fruit fruit = new Fruit();
        fruit.id = "1";
        fruit.name = "Apple";
        fruit.color = "Green";
        given()
                .contentType("application/json")
                .body(fruit)
                .when().post("/fruits")
                .then()
                .statusCode(201);

        // get the Fruit
        Fruit result = get("/fruits/1").as(Fruit.class);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("1", result.id);
        Assertions.assertEquals("Apple", result.name);
        Assertions.assertEquals("Green", result.color);

        // search the Fruit
        List<Fruit> results = get("/fruits/search?color=Green").as(LIST_OF_FRUIT_TYPE_REF);
        Assertions.assertNotNull(results);
        Assertions.assertFalse(results.isEmpty());
        Assertions.assertEquals("1", results.get(0).id);
        Assertions.assertEquals("Apple", results.get(0).name);
        Assertions.assertEquals("Green", results.get(0).color);
    }

}

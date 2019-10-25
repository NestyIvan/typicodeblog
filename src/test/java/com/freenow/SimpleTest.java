package com.freenow;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SimpleTest {

    static final Logger logger =
            LoggerFactory.getLogger(SimpleTest.class);

    @Before
    public void setUp() throws Exception {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.ANY)
                .log(LogDetail.ALL)
                .build();

        RestAssured.requestSpecification = requestSpec;
    }

    @Test
    public void simpleTest(){
        given().pathParam("id", 3).get("/users/{id}")
                .then().body("name", equalTo("Clementine Bauch"));
    }
}

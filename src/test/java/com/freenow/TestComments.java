package com.freenow;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class TestComments {

    static final Logger logger =
            LoggerFactory.getLogger(TestComments.class);
    static List<Map<String, Object>> posts;

    @Before
    public void setUp() throws Exception {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.ANY)
                .log(LogDetail.ALL)
                .build();
        RestAssured.requestSpecification = requestSpec;

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        //Get the list of posts.
        posts = given().get(EndPoints.posts).as(new TypeRef<List<Map<String, Object>>>() {});
    }

    @Test
    public void testComments(){
        List<Map<String, Object>> comments = given().get(EndPoints.comments, posts.get(0).get("id"))
                .as(new TypeRef<List<Map<String, Object>>>() {});
        assertTrue(comments.size() > 0);
    }
}

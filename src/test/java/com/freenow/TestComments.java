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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        assertTrue(posts.size() > 0);//potentially a bug in the test data
    }

    private boolean checkMailFormat(String email, String postID){
        /*This one possible regex to validate emails from many possible others.
        * The rules for validation may vary from project to project.
        * This regex validates following rules:
        * 0) @ sign should be presented
        * 1) A-Z characters allowed
        * 2) a-z characters allowed
        * 3) 0-9 numbers allowed
        * 4) Dots(.), dashes(-) and underscores(_) are allowed
        * 5) Rest all characters are not allowed
        */
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            logger.error(String.format("The email %s of the post %s is in wrong format.", email, postID));
            return false;
        }
        return true;
    }

    @Test
    public void testComments(){
        for(Map<String, Object> post : posts){
        //Map<String, Object> post = posts.get(0);
            String postID = post.get("id").toString();
            List<Map<String, Object>> comments = given().get(EndPoints.comments, postID)
                    .as(new TypeRef<List<Map<String, Object>>>() {});
            //iterate through comments of each post
            for(Map<String, Object> comment : comments) {
                String email = comment.get("email").toString();
                assertTrue(String.format("The email %s of the post %s is in wrong format.", email, postID),
                        checkMailFormat(email, postID));
            }
        }
    }
}

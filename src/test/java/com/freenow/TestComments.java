package com.freenow;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class TestComments {

    static final Logger logger =
            LoggerFactory.getLogger(TestComments.class);
    static List<Map<String, Object>> posts;

    @BeforeClass
    public static void setUp() throws Exception {
        RestAssured.requestSpecification = RestAssuredSettings.requestSpec;
        RestAssured.responseSpecification = RestAssuredSettings.responseSpec;
        //Get the list of posts.
        posts = given().get(EndPoints.posts).as(new TypeRef<List<Map<String, Object>>>() {});
        //potentially a bug in the test data
        assertTrue("No posts have been found for the user",posts.size() > 0);
    }

    private boolean checkMailFormat(String email, String postID){
        /*This is one possible regex to validate emails among many others.
        * The rules for validation may vary from project to project.
        * This particular regex validates following rules:
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
            logger.info(String.format("Validating %d comments of the post %s and title: %s",
                    comments.size(), postID, post.get("title").toString()));
            //iterate through comments of each post
            for(Map<String, Object> comment : comments) {
                String email = comment.get("email").toString();
                assertTrue(String.format("The email %s of the post %s is in wrong format.", email, postID),
                        checkMailFormat(email, postID));
            }
        }
    }
}

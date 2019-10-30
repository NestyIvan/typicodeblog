package com.freenow;

import io.restassured.common.mapper.TypeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class CheckPostWorker implements Runnable {
    static final Logger logger =
            LoggerFactory.getLogger(CheckPostWorker.class);
    private Map<String, Object> post;
    private static final String POST_ID = "id";
    private static final String POST_TITLE = "title";
    private static final String POST_EMAIL = "email";

    public CheckPostWorker(Map<String, Object> post){
        this.post = post;
    }

    @Override
    public void run() {
        checkPost(this.post);
    }

    /*A possible regex to validate emails among many others.
     * The rules for validation may vary from project to project.
     * This particular regex validates following rules:
     * 0) @ sign should be presented
     * 1) A-Z characters allowed
     * 2) a-z characters allowed
     * 3) 0-9 numbers allowed
     * 4) Dots(.), dashes(-) and underscores(_) are allowed
     * 5) Rest all characters are not allowed
     */
    private boolean checkMailFormat(String email, String postID){
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            logger.error(String.format("The email %s of the post %s is in wrong format.", email, postID));
            return false;
        }
        logger.info(String.format("The email %s of the post %s is good.", email, postID));
        return true;
    }

    private void checkPost(Map<String, Object> post){
        String postID = post.get(CheckPostWorker.POST_ID).toString();
        List<Map<String, Object>> comments = given().get(EndPoints.COMMENTS, postID)
                .as(new TypeRef<List<Map<String, Object>>>() {});
        logger.info(String.format("Validating %d comments of the post %s and title: %s",
                comments.size(), postID, post.get(CheckPostWorker.POST_TITLE).toString()));
        //iterate through comments of each post
        for(Map<String, Object> comment : comments) {
            String email = comment.get(CheckPostWorker.POST_EMAIL).toString();
            assertTrue(String.format("The email %s of the post %s is in wrong format.", email, postID),
                    checkMailFormat(email, postID));
        }
    }

}

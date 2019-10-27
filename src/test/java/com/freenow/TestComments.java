package com.freenow;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class TestComments {

    static final Logger logger =
            LoggerFactory.getLogger(TestComments.class);
    static ConcurrentLinkedQueue<Map<String, Object>> concurrentPosts;

    private static final String POST_ID = "id";
    private static final String POST_TITLE = "title";
    private static final String POST_EMAIL = "email";

    @BeforeClass
    public static void setUp() {
        RestAssured.requestSpecification = Settings.requestSpec;
        RestAssured.responseSpecification = Settings.responseSpec;
    }

    private void initPosts(){
        //Get the list of posts.
        List<Map<String, Object>> posts = given()
                .get(EndPoints.POSTS).as(new TypeRef<List<Map<String, Object>>>() {});
        concurrentPosts = new ConcurrentLinkedQueue();
        concurrentPosts.addAll(posts);
        assertTrue("No posts have been found for the user",concurrentPosts.size() > 0);
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
        return true;
    }

    private void checkPost(Map<String, Object> post){
        String postID = post.get(TestComments.POST_ID).toString();
        List<Map<String, Object>> comments = given().get(EndPoints.COMMENTS, postID)
                .as(new TypeRef<List<Map<String, Object>>>() {});
        logger.info(String.format("Validating %d comments of the post %s and title: %s",
                comments.size(), postID, post.get(TestComments.POST_TITLE).toString()));
        //iterate through comments of each post
        for(Map<String, Object> comment : comments) {
            String email = comment.get(TestComments.POST_EMAIL).toString();
            assertTrue(String.format("The email %s of the post %s is in wrong format.", email, postID),
                    checkMailFormat(email, postID));
        }
    }

    private int getCoreNumber(){
        int cores = Runtime.getRuntime().availableProcessors();
        return Math.min(cores, concurrentPosts.size());
    }

    @Test
    public void testComments(){
        initPosts();
        //Init required number of Threads. Each thread will poll the head post
        // and validate comments until there are posts in the Queue
        int cores = getCoreNumber();
        logger.info(String.format("Start processing %d posts with %d threads.",
                concurrentPosts.size(), cores));
        List<Thread> threads = new ArrayList<>();
        for(int i = 0; i < cores; i++){
            Runnable task = () -> {
                while(concurrentPosts.size() > 0){
                    checkPost(concurrentPosts.poll());
                }
            };
            Thread t = new Thread(task);
            threads.add(t);
            t.start();
        }

        for(Thread t : threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                logger.error("Thread was interrupted:" + e.getMessage());
            }
        }
    }
}

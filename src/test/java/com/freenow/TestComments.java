package com.freenow;

import com.freenow.settings.RestAssuredSettings;
import com.freenow.settings.UserSettings;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class TestComments {

    static final Logger logger =
            LoggerFactory.getLogger(TestComments.class);
    private static List<Map<String, Object>> posts;
    private static ThreadPoolExecutor threadPoolExecutor;
    private static final int THREAD_TIMEOUT = 1;

    @BeforeClass
    public static void setUp() {
        RestAssured.requestSpecification = RestAssuredSettings.requestSpec;
        RestAssured.responseSpecification = RestAssuredSettings.responseSpec;
        initPosts();
        threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(getCoreNumber());
    }

    @AfterClass
    public static void cleanUp() throws InterruptedException {
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(THREAD_TIMEOUT, TimeUnit.MINUTES);
    }

    private static void initPosts(){
        //Get the list of posts.
        posts = given().get(EndPoints.POSTS, UserSettings.getUserID())
                .as(new TypeRef<List<Map<String, Object>>>() {});
        assertTrue("No posts have been found for the user",
                posts.size() > 0);
    }

    /*
     * This should be at least discussed in the team.
     * There could be different reasons to modify the logic of this method.
     * What kind of the environment is going to be used?
     * What are the other tests and their priority?
     */
    private static int getCoreNumber(){
        int cores = Runtime.getRuntime().availableProcessors();
        return Math.min(cores, posts.size());
    }

    @Test
    public void testPostResponseTime(){
        Response response = given().get(EndPoints.POSTS, UserSettings.getUserID());
        assertTrue("The response time for post endpoint exceeds",
                response.time() < RestAssuredSettings.TIME_OUT);
        logger.info(String.format("Response time for posts written by user %s was %d ms.",
                UserSettings.USER_NAME, response.time()));
    }

    @Test
    public void testComments(){
        logger.info(String.format("Start processing %d posts with %d threads.",
                posts.size(), getCoreNumber()));
        for(Map<String, Object> post : posts){
            CheckPostWorker task = new CheckPostWorker(post);
            threadPoolExecutor.execute(task);
        }
    }
}

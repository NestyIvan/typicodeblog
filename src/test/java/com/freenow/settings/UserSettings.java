package com.freenow.settings;

import com.freenow.EndPoints;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserSettings {
    static final Logger logger =
            LoggerFactory.getLogger(UserSettings.class);

    public static final String USER_NAME = System.getProperty("user.name");

    private static final String USER_ID = "id";
    private static int userID;
    private static UserSettings instance;

    private UserSettings(){
        RestAssured.requestSpecification = RestAssuredSettings.requestSpec;
        RestAssured.responseSpecification = RestAssuredSettings.responseSpec;
        List<Map<String, Object>> user = given()
                .get(EndPoints.USER_BY_NAME, UserSettings.USER_NAME)
                .as(new TypeRef<List<Map<String, Object>>>() {});
        if(user == null || user.get(0) == null){
            logger.error(String.format("The user with the name %s was not found",
                    UserSettings.USER_NAME));
        }
        userID = Integer.parseInt(user.get(0).get(UserSettings.USER_ID).toString());
    }

    public static UserSettings getInstance(){
        if(instance == null){
            instance = new UserSettings();
        }
        return instance;
    }

    public static int getUserID(){
        return UserSettings.getInstance().userID;
    }
}

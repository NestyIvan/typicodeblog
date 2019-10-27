package com.freenow;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public final class Settings {
    //TODO: use environment\gradle variable instead
    public static String baseURI = "https://jsonplaceholder.typicode.com";
    public static final int samanthaID = 3;

    public static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(baseURI)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.ANY)
            .log(LogDetail.ALL)
            .build();

    public static final ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
}

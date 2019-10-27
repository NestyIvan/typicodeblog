package com.freenow.pojos;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public final class RestAssuredSettings {
    public static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://jsonplaceholder.typicode.com")
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.ANY)
            .log(LogDetail.ALL)
            .build();

    public static final ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
}

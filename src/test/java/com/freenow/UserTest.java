package com.freenow;

import com.freenow.pojos.Address;
import com.freenow.pojos.Company;
import com.freenow.pojos.Geo;
import com.freenow.pojos.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * A test cases to verify the user Samantha
 */
public class UserTest {
    static final Logger logger =
            LoggerFactory.getLogger(UserTest.class);

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
        RestAssured.responseSpecification = responseSpec;
    }

    @Test
    public void testSamanthaProfile(){
        User user = given().get(EndPoints.user).as(User.class);
        assertThat(user.name, equalTo("Clementine Bauch"));
        assertThat(user.id, equalTo(3));
        assertThat(user.username, equalTo("Samantha"));
        assertThat(user.email, equalTo("Nathan@yesenia.net"));
        assertThat(user.phone, equalTo("1-463-123-4447"));
        assertThat(user.website, equalTo("ramiro.info"));

        Address address = user.address;
        assertThat(address.street, equalTo("Douglas Extension"));
        assertThat(address.suite, equalTo("Suite 847"));
        assertThat(address.city, equalTo("McKenziehaven"));
        assertThat(address.zipcode, equalTo("59590-4157"));

        Geo geo = address.geo;
        assertThat(geo.lat, equalTo("-68.6102"));
        assertThat(geo.lng, equalTo("-47.0653"));

        Company company = user.company;
        assertThat(company.name, equalTo("Romaguera-Jacobson"));
        assertThat(company.catchPhrase, equalTo("Face to face bifurcated interface"));
        assertThat(company.bs, equalTo("e-enable strategic applications"));
    }
}

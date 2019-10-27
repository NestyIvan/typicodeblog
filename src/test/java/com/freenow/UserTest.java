package com.freenow;

import com.freenow.pojos.*;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
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

    @BeforeClass
    public static void setUp() throws Exception {
        RestAssured.requestSpecification = Settings.requestSpec;
        RestAssured.responseSpecification = Settings.responseSpec;
    }

    @Test
    public void testSamanthaProfile(){
        User user = given().get(EndPoints.user).as(User.class);
        assertThat(user.name, equalTo("Clementine Bauch"));
        assertThat(user.id, equalTo(Settings.samanthaID));
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

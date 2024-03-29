package com.freenow;

import com.freenow.pojos.Address;
import com.freenow.pojos.Company;
import com.freenow.pojos.Geo;
import com.freenow.pojos.User;
import com.freenow.settings.RestAssuredSettings;
import com.freenow.settings.UserSettings;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * A test cases to verify the user
 */
public class UserTest {
    static final Logger logger =
            LoggerFactory.getLogger(UserTest.class);

    @BeforeClass
    public static void setUp() {
        RestAssured.requestSpecification = RestAssuredSettings.requestSpec;
        RestAssured.responseSpecification = RestAssuredSettings.responseSpec;
    }

    @Test
    public void testResponseTime(){
        Response response = given().get(EndPoints.USER, UserSettings.getUserID());
        assertTrue("The response time for user endpoint exceeds",
                response.time() < RestAssuredSettings.TIME_OUT);
    }

    @Test
    public void testSamanthaProfile(){
        User user = given().get(EndPoints.USER, UserSettings.getUserID()).as(User.class);
        assertThat(user.name, equalTo("Clementine Bauch"));
        //assertThat(user.id, equalTo(RestAssuredSettings.SAMANTHA_ID));
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

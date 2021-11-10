package in.reqres;

import dto.UserList;
import dto.UserLogin;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.testng.AssertJUnit.assertEquals;

public class ApiTest {

    @Test
    public void firstTest() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec());
        given()
                .when()
                .get("api/users?page=2")
                .then()
                .log().all();
    }

    @Test
    public void secondTest() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec());
        given()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().all()
                .body("page", equalTo(2))
                .body("data[0].id", equalTo(7))
                .body("total", notNullValue());
    }

    @Test
    public void thirdTest() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec());
        Response response = given()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().all()
                .body("page", equalTo(2))
                .body("data[0].id", equalTo(7))
                .body("total", notNullValue())
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getInt("page"), 2, "Page is not 2");
    }

    @Test
    public void fourTest() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec());
        given()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().all()
                .body("page", equalTo(2))
                .body("data[0].id", equalTo(7))
                .body("total", notNullValue())
                .extract().as(UserList.class);
    }

    @Test
    public void fiveTest() {
        UserLogin userLogin = new UserLogin("eve.holt@reqres.in", "cityslicka");
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec());
        given()
                .body(userLogin)
                .when()
                .post("/api/login")
                .then()
                .log().all();
    }

    @Test
    public void sixTest() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec());
        Response response = given()
                .when()
                .get("/api/unknown")
                .then()
                .log().all()
                .body("data.year", hasItems(2001))
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getInt("per_page"), 6, "Per_page is not 6");

        String value = response.path("data.color").toString();

        Pattern pattern = Pattern.compile("#[0-9A-Z]{6}");
        Matcher matcher = pattern.matcher(value);

        assertEquals(6, matcher.results().count());
    }

    @Test
    public void sevenTest() {
        UserLogin userLogin = new UserLogin("eve.holt@reqres.in", "pistol");
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec());
        given()
                .body(userLogin)
                .when()
                .post("/api/register")
                .then()
                .log().all();
    }

    @Test
    public void eightTest() {
        Specification.installSpec(Specification.requestSpec(), Specification.specNull());
        UserLogin userLogin = new UserLogin("eve.holt@reqres.in", "");
        Response response = given()
                .body(userLogin)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .log().all()
                .extract().response();
        Assert.assertEquals(400, response.statusCode());
    }

    @Test
    public void nineTest() {
        Specification.installSpec(Specification.requestSpec(), Specification.specNull());
        Response response = given()
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204)
                .log().all()
                .extract().response();
        Assert.assertEquals(204, response.statusCode());
    }
}

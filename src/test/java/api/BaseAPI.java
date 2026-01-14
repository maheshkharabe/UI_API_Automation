package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import utils.TestConfigSetUp;

public class BaseAPI {

    protected String pathCars="/api/cars";

    public Response getCars(){
        Response response= RestAssured
                .given()
                    .baseUri(TestConfigSetUp.get("baseURI"))
                    .accept("application/json")
                .when()
                    .get(pathCars)
                .then()
                    .log().all()
                    .extract().response();

        return response;

    }
    public Response getCars(int expStatusCode){
        Response response= RestAssured
                .given()
                    .baseUri(TestConfigSetUp.get("baseURI"))
                    .accept("application/json")
                .when()
                    .get(pathCars)
                .then()
                    .statusCode(expStatusCode)
                    .extract().response();

        return response;

    }
}

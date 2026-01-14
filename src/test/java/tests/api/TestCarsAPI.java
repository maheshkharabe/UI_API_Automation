package tests.api;

import api.BaseAPI;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.TestConfigSetUp;

import java.util.HashMap;
import java.util.List;

public class TestCarsAPI extends BaseAPI {
    int expectedStatusCode=200;
    long expectedResponseTime =5000;
    String expectedCurrencySign="€";
    String expectedResponseContentType="application/json; charset=utf-8";

    @Test(groups = {"SANITY"})
    public void test01_VerifyResponseStatusCode(){
        Response resp= getCars();
        Assert.assertEquals(resp.getStatusCode(),expectedStatusCode,"Incorrect response status code for get cars");
    }

    @Test(groups = {"SANITY"})
    public void test02_VerifyResponseContentsAreJson(){
        Response resp= getCars();
        Assert.assertEquals(resp.contentType(),expectedResponseContentType,"Incorrect response content type");
    }

    @Test(groups = {"REGRESSION"})
    public void test03_VerifyResponseBodyDataAgainstSchema(){
        ValidatableResponse valResp= getCars().then();
        valResp.assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("Schema_Cars.json"));
    }

    @Test(groups = {"REGRESSION"})
    public void test04_VerifyCurrencySignIsPresentInPriceTag(){
        Response resp= getCars();
        SoftAssert sw=new SoftAssert();
        JsonPath jPath = resp.jsonPath();
        List<String> priceList = jPath.get("cars.price");//extract all values for price
        for (String s:priceList) {//iterate through each price value to check if contains currency sign
            sw.assertTrue(s.contains(expectedCurrencySign),"Currency sign '"+expectedCurrencySign+"' is missing in price");
        }
        sw.assertAll();
    }

    @Test(groups = {"REGRESSION"})
    public void test05_VerifyEachCarHasUniqueId(){

        Response resp= getCars();
        JsonPath jPath = resp.jsonPath();
        List<Integer> idList = jPath.get("cars.id");//extract all values for id
        long uniqueIdCount = idList.stream().distinct().count();//count oif distinct values should match size of list
        Assert.assertEquals(uniqueIdCount,idList.size(),"Id contains duplicate values");
    }

    @Test(groups = {"REGRESSION"})
    public void test06_VerifyRecordsAgainstDataBase(){
        Response resp= getCars();
        JsonPath jPath = resp.jsonPath();
        List<HashMap> allCars = jPath.get("cars");
        System.out.println("Number of cars:"+allCars.size());
        for (HashMap car:allCars) {
            System.out.println("Car "+car);
            //car.get("id");
            //car.get("name");
            /*********************************************************************************************************
            If we have access to Data base
            We can check if number of records in DB matches with get response meaning, service is returning all records
            For each 'car' from get service response we can validate its details(name, price etc.) against DB
            *********************************************************************************************************/
        }
    }
    @Test(groups = {"REGRESSION"})
    public void test07_missingHeaderOnRequestDefaultsJsonResponse(){
        Response response= RestAssured
                .given()
                    .baseUri(TestConfigSetUp.get("baseURI"))
                .when()
                    .get(pathCars)
                .then()
                    .log().all()
                    .statusCode(expectedStatusCode)
                    .contentType(ContentType.JSON)
                    .extract().response();
    }

    @Test(groups = {"PERFORMANCE"})
    public void test08_VerifyResponseTime(){
        Response resp= getCars();
        long responseTimeMS=resp.getTime();
        System.out.println("Service response time:"+responseTimeMS);
        Assert.assertTrue(responseTimeMS<expectedResponseTime,"Service response time is longer than expected");
    }

}//end class

package tests.ui;

import api.BaseAPI;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ui.BaseUI;
import ui.CarsPage;
import ui.HomePage;
import utils.TestConfigSetUp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

public class TestCarsUI extends BaseUI {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    BaseAPI objBaseApi=new BaseAPI();
    HomePage objHomePage;
    String expectedHomePageTile="Automation Testing Practice Website for QA and Developers | UI and API";
    String expectedCarPageTile="Cars Showroom page for Automation Testing Practice";


    @BeforeMethod(alwaysRun=true)
    public void launchWebURL(ITestContext testContext){
        WebDriver dw =getBrowserSpecificDriver();
        driver.set(dw);
        driver.get().get(TestConfigSetUp.get("baseURI"));
        objHomePage = new HomePage(driver.get());
        testContext.setAttribute("attributeDriver",driver.get());
    }

    @Test(groups = {"SANITY"})
    public void test01_VerifyPageTitle(){
        SoftAssert sw = new SoftAssert();
        String actualHomePageTitle= objHomePage.getPageTitle();
        sw.assertEquals(actualHomePageTitle,expectedHomePageTile,"Home page title is incorrect");
        objHomePage.clickOnCarsShowroom();
        CarsPage objCarPage = new CarsPage(driver.get());
        String actualTile = objCarPage.getPageTitle();
        sw.assertEquals(actualTile,expectedCarPageTile,"Cars Showroom Page title is incorrect");
        sw.assertAll("Few validations failed for test: checking page titles");
    }

    @Test(groups = {"REGRESSION"})
    public void test02_VerifyAllCarsAreListed(){
        Response resp = objBaseApi.getCars(200);
        List<HashMap> allCars = resp.path("cars");

        objHomePage.clickOnCarsShowroom();
        CarsPage objCarPage = new CarsPage(driver.get());
        List<WebElement> webElementsCars = objCarPage.getAllWebEleForCarsDisplayed();

        Assert.assertEquals(webElementsCars.size(),allCars.size(),"All cars are not listed on UI");
    }

    @Test(groups = {"REGRESSION"})
    public void test03_VerifyCarDetailsDisplayedAreCorrect(){
        SoftAssert softAssert = new SoftAssert();
        //Get Cars from API
        Response resp = objBaseApi.getCars(200);
        List<HashMap> allCarsFromAPI = resp.path("cars");

        objHomePage.clickOnCarsShowroom();
        CarsPage objCarPage = new CarsPage(driver.get());
        List<WebElement> webElementsCars = objCarPage.getAllWebEleForCarsDisplayed();

        softAssert.assertEquals(webElementsCars.size(),allCarsFromAPI.size(),"Number of Cars displayed on UI does not match with API response list");

        for(int i=0;i<allCarsFromAPI.size() && i<webElementsCars.size();i++){
            WebElement weCar;
            //For each record of car in API response, extract details and compare with UI displayed data
            //Car Details from API
            String carIdFromAPI = allCarsFromAPI.get(i).get("id").toString();
            String carNameFromAPI = allCarsFromAPI.get(i).get("name").toString();
            String carPriceFromAPI = allCarsFromAPI.get(i).get("price").toString();
            String carImageUrlFromAPI = allCarsFromAPI.get(i).get("image").toString();

            //Car details displayed on UI
            weCar= objCarPage.getIndividualCarDisplayName(carIdFromAPI);
            String carDisplayName=weCar.getText();
            weCar= objCarPage.getIndividualCarImageUrls(carIdFromAPI);
            String carImageUrl=weCar.getAttribute("src");
            weCar= objCarPage.getIndividualCarPrice(carIdFromAPI);
            String carDisplayPrice=weCar.getText();
            //System.out.println("CarName:"+carDisplayName+" ,Image:"+carImageUrl+" ,Price:"+carDisplayPrice);

            //Compare data is correctly displayed
            softAssert.assertEquals(carDisplayName.trim(),carNameFromAPI,"Car Name is incorrectly displayed");
            softAssert.assertEquals(carDisplayPrice,carPriceFromAPI,"Car Price is incorrectly displayed");
            softAssert.assertTrue(carImageUrl.contains(carImageUrlFromAPI),"Car Image is incorrectly displayed");
        }
        softAssert.assertAll("Few validations failed for test: cars details are correctly displayed as per API response");
    }

    @Test(groups = {"REGRESSION"})
    public void test04_VerifyCarImagesAreNotBroken(){
        SoftAssert sw=new SoftAssert();
        objHomePage.clickOnCarsShowroom();
        CarsPage objCarPage = new CarsPage(driver.get());
        List<WebElement> allCarsImages = objCarPage.getAllWebEleForImages();

        for (WebElement image:allCarsImages) {
            //get url from each image tag with its attribute 'src'
            String urlString = image.getAttribute("src");
            //Create URL connection
            try {
                URL url = new URL(urlString); //convert string url to java URL object
                URLConnection urlConn = url.openConnection(); // create connection for URL
                HttpURLConnection httpUrl = (HttpURLConnection) urlConn; //Type cast to HTTP connection
                httpUrl.setConnectTimeout(10000);//set timeout to wait for connection establishment before timeout
                httpUrl.connect();//send request to server using 'connect()'

                /*if(httpUrl.getResponseCode()==200){
                    System.out.println("Good Image: "+urlString+" >> "+ httpUrl.getResponseCode() + " >> "+ httpUrl.getResponseMessage());
                }else{
                    System.out.println("Broken Image: "+urlString+" >> "+ httpUrl.getResponseCode() + " >> "+ httpUrl.getResponseMessage());
                }*/
                sw.assertEquals(httpUrl.getResponseCode(),200,"Broken Image found:"+urlString);
                httpUrl.disconnect();

            } catch (Exception e) {
                System.err.println("Exception handling on image, also counted as broken image:"+urlString);
                e.printStackTrace();
            }
        }//end foreach
        sw.assertAll("Few validations failed for test: cars images are broken or not");
    }

    @Test(groups = {"REGRESSION"})
    public void test05_VerifyCarNameDisplayedIsHyperLink() {
        SoftAssert sw=new SoftAssert();
        objHomePage.clickOnCarsShowroom();
        CarsPage objCarPage = new CarsPage(driver.get());
        List<WebElement> allCarNames = objCarPage.getAllWebEleForCarNames();

        for (WebElement car : allCarNames) {
            List<WebElement> hyperLink = car.findElements(By.xpath("a[@href]"));
            /*if(!hyperLink.isEmpty()){
                System.out.println("Car Name:"+car.getText()+" ,is hyper link");
            }else{
                System.out.println("Car Name:"+car.getText()+" ,is NOT hyper link");
            }*/
            sw.assertTrue(!hyperLink.isEmpty(),"Car Name:"+car.getText()+" ,is NOT hyper link");
        }
        sw.assertAll("Few validations failed for test: checking car names are hyper links");
    }

    @AfterMethod(alwaysRun = true)
    public void closeCurrentBrowserWindow(){
        driver.get().quit();
        driver.remove();
    }
}

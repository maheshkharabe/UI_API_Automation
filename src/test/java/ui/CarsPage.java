package ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CarsPage {

    WebDriver driver;
    WebDriverWait wait;

    public CarsPage(WebDriver d){
        driver = d;
        PageFactory.initElements(driver,this);//Create web elements for current page
    }

    public String getPageTitle(){
        return driver.getTitle();
    }

    private By carListLocator = By.xpath("//div[@id='car-list']//div[contains(@data-testid,'car')]");

    private By allCarsImageUrls = By.xpath("//div[@id='car-list']//div[contains(@data-testid,'car')]/img");

    private By allCarDisplayNames = By.xpath("//div[@id='car-list']//div[contains(@data-testid,'car')]/div/h5");


    public List<WebElement> getAllWebEleForCarsDisplayed(){
        wait = new WebDriverWait(driver,60);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(carListLocator));
        List<WebElement> allCars = driver.findElements(carListLocator);
        return allCars;
    }

    public List<WebElement> getAllWebEleForImages(){
        wait = new WebDriverWait(driver,60);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allCarsImageUrls));
        List<WebElement> allCarImages = driver.findElements(allCarsImageUrls);
        return allCarImages;
    }

    public List<WebElement> getAllWebEleForCarNames(){
        wait = new WebDriverWait(driver,60);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allCarDisplayNames));
        List<WebElement> allCarNames = driver.findElements(allCarDisplayNames);
        return allCarNames;
    }

    public WebElement getIndividualCarImageUrls(String id){
        String carXpath = "//div[contains(@data-testid,'car') and contains(@data-testid,'"+id+"')]/img";
        return driver.findElement(By.xpath(carXpath));
    }
    public WebElement getIndividualCarPrice(String id){
        String carXpath = "//div[contains(@data-testid,'car') and contains(@data-testid,'"+id+"')]/div/p";
        return driver.findElement(By.xpath(carXpath));
    }
    public WebElement getIndividualCarDisplayName(String id){
        String carXpath = "//div[contains(@data-testid,'car') and contains(@data-testid,'"+id+"')]/div/h5";
        return driver.findElement(By.xpath(carXpath));
    }
}

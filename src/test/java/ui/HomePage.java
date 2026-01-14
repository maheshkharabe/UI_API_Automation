package ui;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    WebDriver driver;
    WebDriverWait wait;
    public HomePage(WebDriver d){
        driver = d;
        PageFactory.initElements(driver,this);
    }

    public String getPageTitle(){
        return driver.getTitle();
    }

    @FindBy(linkText = "Cars Showroom")
    private WebElement carsShowRoom;

    public void clickOnCarsShowroom(){
        wait =new WebDriverWait(driver,60);
        wait.until(ExpectedConditions.elementToBeClickable(carsShowRoom));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", carsShowRoom);
        js.executeScript("arguments[0].click();", carsShowRoom);
    }
}

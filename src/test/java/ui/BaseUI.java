package ui;

import com.google.common.collect.ImmutableMap;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.TestConfigSetUp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BaseUI {
    WebDriver wbDriver;
    private String browser;

    public WebDriver getBrowserSpecificDriver() {
        browser= TestConfigSetUp.get("browser");

        switch (browser.toUpperCase()){
            case "FIREFOX":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fxOptions = new FirefoxOptions();
                fxOptions.addArguments("--start-maximized");
                wbDriver = new FirefoxDriver();
                break;

            case "CHROME":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                wbDriver = new ChromeDriver(chromeOptions);
                break;

            default:
                 System.out.println("************ Provide one of browsers (Chrome,Firefox) ************");
        }

        wbDriver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);//if not set selenium defaults wait is 300 sec.
        return wbDriver;
    }// end navigateToHomePage

}//end class

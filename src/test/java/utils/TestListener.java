package utils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;

public class TestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("attributeDriver");
        Throwable error = result.getThrowable();
        String packageName = result.getTestClass().getRealClass().getPackage().getName();
        //System.out.print("Package:"+packageName);
        if(packageName.contains("ui")){//Take screen print in case of UI tets failure
            saveScreenPrintAsFile(driver,result.getName());
        }
        System.err.println("*********************TEST FAILURE ******************\n"+result.getMethod()+", \nErrors:"+error);
    }

    public static void saveScreenPrintAsFile(WebDriver driver,String fileName){
        File screenPrint = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String pathToSave= TestConfigSetUp.get("pathToScreenShots")+ fileName+ ".png";
        try {
            FileUtils.copyFile(screenPrint, new File(pathToSave));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//saveScreenPrintAsFile
}


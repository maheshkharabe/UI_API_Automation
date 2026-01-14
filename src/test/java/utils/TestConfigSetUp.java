package utils;

import org.testng.annotations.BeforeSuite;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class TestConfigSetUp {

    private static final Properties testProperties= new Properties();
    private static String environment = System.getProperty("env", "sit");//read System property-env, if not provided default to SIT

    @BeforeSuite(alwaysRun=true)
    public static void setConfigs() {
        String fileName;
        System.out.println("********************* Setting up test configs for environment:"+environment);
        //Set up configs based on Env value e.g. dev,sit,uat etc.
        //default to sit
        if(environment.toLowerCase().equals("uat")){
            fileName = "/uat_config.properties";
        }else{
            fileName = "/sit_config.properties";
        }
        try (InputStream input = TestConfigSetUp.class.getResourceAsStream(fileName)) {
            if (Objects.isNull(input)) {
                throw new IllegalStateException("Config file not found: " + fileName);
            }
            testProperties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load config file: " + fileName, e);
        }
    }//end setConfig

    public static String get(String key) {
        String value = testProperties.getProperty(key);
        //System.out.println("Value:"+value);
        if (value == null) {
            throw new IllegalArgumentException("Property not found: " + key);
        }
        return value.trim();
    }


}//end TestConfigSetUp

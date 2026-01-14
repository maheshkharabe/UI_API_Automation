****************************** Automation Framework ******************************


###Overview:
This repository contains a Maven based TestNG automation framework for both API and UI testing.
- API tests built with RestAssured for validating RESTful services.
- UI tests built with Selenium WebDriver, supporting parallel execution using a ThreadLocal WebDriver.
- TestNG suite files are maintained separately for API and UI execution.
- Custom TestListener captures screenshots automatically when UI tests fail.

Framework uses https://practice.expandtesting.com as target application.

API docs are available on: https://practice.expandtesting.com/api/api-docs 

###Environment Specific Configuration:
The framework supports environment specific properties files to manage different test environments such as SIT and UAT.
Location-
- Properties files under src/test/resources/
  Behavior-
- At runtime the framework checks the system property 'env'.
- If -Denv is provided the framework loads the corresponding file (for example uat_config.properties when -Denv=uat).
- If -Denv is not provided the framework defaults to sit_config.properties.
  
  Example usage-
  
  -Run tests with default SIT environment:
  mvn test -DsuiteXmlFile=src/test/resources/API_Runner.xml
  
  -Run tests with UAT environment:
  mvn test -Denv=UAT -DsuiteXmlFile=src/test/resources/API_Runner.xml

###Setup and Running Tests Locally:
Prerequisites-
- Java 8 or later with JAVA_HOME set.
- Maven 3.6 or later.
- Latest Chrome or Firefox browsers.
- Clone repository
  git clone https://github.com/......
  cd <your-repo>
- Install dependencies
  mvn clean install -DskipTests
- Run suiteXML files

Notes for local UI runs
- For local debugging you may run Chrome in headed mode; CI requires headless mode and CI flags.
- Screenshots for failed UI tests are saved under target/TestScreenPrints/. This is also taken from .properties file.
- Use -Denv to switch environments as shown in the Environment Specific Configuration section

###Project structure

<img width="364" height="146" alt="image" src="https://github.com/user-attachments/assets/967628ac-18be-4bdb-bec5-6b0992cbcbfa" />


###CI Integration
GitHub Actions workflow
- Workflow triggers on push and pull_request.
- Install dependencies
- Runs API and UI tests
- Generate test artifacts
	 
	 

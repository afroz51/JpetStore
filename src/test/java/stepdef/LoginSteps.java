package stepdef;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;

import base.BaseClass;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.LoginPage;

public class LoginSteps extends BaseClass 
{
    LoginPage loginPage;

    @Given("User navigates to the login page")
    public void userNavigatesToLoginPage() {
        test = extent.createTest("User Login Test");  // Initialize the test case in Extent Reports
        test.info("Navigating to Login Page");

        driver.get(prop.getProperty("url"));  // Open the application URL from properties file
        loginPage = new LoginPage(driver);  // Initialize LoginPage object
        loginPage.openLoginPage();  // Navigate to login page
        test.pass("User successfully navigated to login page");
    }

    @When("User enters invalid data table login details")
    public void userEntersInvalidDataTable(io.cucumber.datatable.DataTable dataTable) throws IOException, InterruptedException 
    {
        test.info("Entering invalid login details using DataTable");

        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);  // Convert DataTable to List of Maps

        for (Map<String, String> row : data) {
            loginPage.login(row.get("username"), row.get("password"));  // Perform login for each data set
        }
        loginPage.submit();

        test.pass("Invalid login form submitted");
    }

    @When("User enters valid data table login details")
    public void userEntersValidDataTable(io.cucumber.datatable.DataTable dataTable) throws IOException, InterruptedException 
    {
        test.info("Entering valid login details using DataTable");

        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);  // Convert DataTable to List of Maps

        for (Map<String, String> row : data) {
            loginPage.login(row.get("username"), row.get("password"));  // Perform login for each data set
        }
        loginPage.submit();
        
        test.pass("Valid login form submitted");
    }

    @When("User enters invalid excel login details")
    public void userEntersInvalidExcelDetails() throws IOException, InterruptedException 
    {
    	loadExcelData();
    	test.info("Entering invalid login details using Excel data");

        loginPage.login(getCellData(1, 0), getCellData(1, 1));  // Fetch username and password from Excel sheet
        loginPage.submit();
        
        test.pass("Invalid login details from Excel submitted");
    }

    @When("User enters valid properties login details")
    public void userEntersValidPropertiesDetails() throws IOException, InterruptedException
    {
    	loadProperties();
        test.info("Entering valid login details using Properties file");

        loginPage.login(prop.getProperty("userid"), prop.getProperty("newpassword"));  // Fetch credentials from properties file
        loginPage.submit();
        
        test.pass("Valid login details from Properties file submitted");
    }

    @Then("Error message should be displayed")
    public void verifyErrorMessage() throws IOException, InterruptedException 
    {
        try {
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message is not displayed");  // Assert error message visibility
            test.pass("Error message displayed successfully");
        } 
        catch (AssertionError e)
        {
            String screenshotPath = screenshot();  // Capture screenshot on failure
            test.fail("Error message not displayed");
            test.addScreenCaptureFromPath(screenshotPath);  // Attach screenshot to Extent Report
        }
    }

    @Then("User should be logged in successfully")
    public void verifySuccessfulLogin() throws IOException, InterruptedException
    {
        try {
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Login failed");  // Assert login success
            test.pass("User logged in successfully");
        } 
        catch (AssertionError e) 
        {
            String screenshotPath = screenshot();  // Capture screenshot on failure
            test.fail("Login failed");
            test.addScreenCaptureFromPath(screenshotPath);  // Attach screenshot to Extent Report
        }
    }
}

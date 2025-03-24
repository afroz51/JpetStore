package stepdef;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import base.BaseClass;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.RegistrationPage;

public class RegistrationSteps extends BaseClass
{
    RegistrationPage regPage;

    @Given("User navigates to the registration page")
    public void userNavigatesToRegistrationPage() 
    {
        test = extent.createTest("User Registration Test");  // Initialize Extent Report Logging
        test.info("Navigating to Registration Page");

        driver.get(prop.getProperty("url"));  // Open application URL from properties file
        regPage = new RegistrationPage(driver);  // Initialize RegistrationPage object
        regPage.openRegistrationPage();  // Navigate to registration page
        test.pass("User successfully navigated to registration page");
    }

    @When("User enters invalid data table data registration details")
    public void userEntersInvalidDetails(io.cucumber.datatable.DataTable dataTable) throws IOException, InterruptedException 
    {
        test.info("Entering invalid registration details using DataTable");

        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);  // Convert DataTable to List of Maps
        
        for (Map<String, String> row : data) {
            regPage.fillRegistrationForm(  // Fill the registration form using values from DataTable
                row.get("username"), row.get("password"), row.get("repeatedPassword"),
                row.get("firstName"), row.get("lastName"), row.get("email"), row.get("phone"),
                row.get("address1"), row.get("address2"), row.get("city"), row.get("state"),
                row.get("zip"), row.get("country"), row.get("language"), 
                row.get("favoriteCategory"), Boolean.parseBoolean(row.get("enableMyList")), 
                Boolean.parseBoolean(row.get("enableMyBanner"))
            );
        }
        regPage.submitForm();  // Submit the form
        test.pass("Invalid registration form submitted");
    }

    @When("User enters valid data table data registration details")
    public void userEntersValidDetails(io.cucumber.datatable.DataTable dataTable) throws IOException, InterruptedException 
    {
        test.info("Entering valid registration details using DataTable");

        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);  // Convert DataTable to List of Maps
        
        for (Map<String, String> row : data) {
            regPage.fillRegistrationForm(  // Fill the registration form using values from DataTable
                row.get("username"), row.get("password"), row.get("repeatedPassword"),
                row.get("firstName"), row.get("lastName"), row.get("email"), row.get("phone"),
                row.get("address1"), row.get("address2"), row.get("city"), row.get("state"),
                row.get("zip"), row.get("country"), row.get("language"), 
                row.get("favoriteCategory"), Boolean.parseBoolean(row.get("enableMyList")), 
                Boolean.parseBoolean(row.get("enableMyBanner"))
            );
        }
        regPage.submitForm();  // Submit the form
        test.pass("Valid registration form submitted");
    }
    
    @When("User enters invalid excel registration details")
    public void userEntersInvalidDetails() throws IOException, InterruptedException 
    {
    	loadExcelData();
        test.info("Entering invalid registration details using Excel data");

        regPage.fillRegistrationForm(  // Fetch user registration details from Excel sheet
            getCellData(1, 0), getCellData(1, 1), getCellData(1, 2), 
            getCellData(1, 3), getCellData(1, 4), getCellData(1, 5), 
            getCellData(1, 6), getCellData(1, 7), getCellData(1, 8), 
            getCellData(1, 9), getCellData(1, 10), getCellData(1, 11), 
            getCellData(1, 12), getCellData(1, 13), getCellData(1, 14), 
            Boolean.parseBoolean(getCellData(1, 15)), 
            Boolean.parseBoolean(getCellData(1, 16))
        );        

        regPage.submitForm();  // Submit the form
        test.pass("Invalid registration details from Excel submitted");
    }
    
    @When("User enters valid properties registration details")
    public void userEntersValidDetails() throws IOException, InterruptedException 
    {
    	loadProperties();
        test.info("Entering valid registration details using Properties file");

        regPage.fillRegistrationForm(  // Fetch registration details from properties file
            prop.getProperty("userid"), prop.getProperty("newpassword"), 
            prop.getProperty("repeatpassword"), prop.getProperty("firstname"), 
            prop.getProperty("lastname"), prop.getProperty("email"), 
            prop.getProperty("phone"), prop.getProperty("address1"), 
            prop.getProperty("address2"), prop.getProperty("city"), 
            prop.getProperty("state"), prop.getProperty("zip"), 
            prop.getProperty("country"), prop.getProperty("language"), 
            prop.getProperty("favuiorate_category"), 
            Boolean.parseBoolean(prop.getProperty("Enable_Mylist")), 
            Boolean.parseBoolean(prop.getProperty("Enable_MyBanner"))
        );

        regPage.submitForm();  // Submit the form
        test.pass("Valid registration details from Properties file submitted");
    }

    @Then("Error message should appear for missing details")
    public void verifyErrorMessage1() throws InterruptedException, IOException 
    {
        try {
            Assert.assertTrue("Error message is not displayed", regPage.isErrorMessageDisplayed());  // Validate if error message is displayed
            test.pass("Error message displayed successfully");
        } 
        catch (AssertionError e)
        {
            String screenshotPath = screenshot();  // Capture screenshot if validation fails
            test.fail("Error message not displayed");
            test.addScreenCaptureFromPath(screenshotPath);  // Attach screenshot to Extent Report
        }
    }
    
    @Then("User should be registered successfully")
    public void verifySuccessfulRegistration() throws IOException, InterruptedException 
    {
        try {
            Assert.assertTrue("Registration failed", regPage.isRegistrationSuccessful());  // Validate if registration is successful
            test.pass("User registered successfully");
        } 
        catch (AssertionError e) 
        {
            String screenshotPath = screenshot();  // Capture screenshot if registration fails
            test.fail("Registration failed");  // Log failure message
            test.addScreenCaptureFromPath(screenshotPath);  // Attach screenshot to Extent Report
        }
    }
}

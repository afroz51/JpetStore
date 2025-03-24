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
import pages.LogoutPage;

public class LogoutSteps extends BaseClass 
{
    LoginPage loginPage;
    LogoutPage logoutPage;

    @Given("User is logged in with valid credentials")
    public void userIsLoggedIn(io.cucumber.datatable.DataTable dataTable) throws IOException, InterruptedException 
    {
        test = extent.createTest("User Logout Test");  // Initialize the test case in Extent Reports
        test.info("Logging in with valid credentials");

        driver.get(prop.getProperty("url"));  // Open the application URL
        loginPage = new LoginPage(driver);
        loginPage.openLoginPage();

        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);  // Convert DataTable to List of Maps
        for (Map<String, String> row : data) {
            loginPage.login(row.get("username"), row.get("password"));  // Perform login
        }
        loginPage.submit();

        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login failed");
        test.pass("User logged in successfully");

        logoutPage = new LogoutPage(driver);  // Initialize LogoutPage object
    }

    @When("User clicks on {string} from {string}")
    public void userClicksOnLogout(String button, String section) throws IOException 
    {
        test.info("Clicking on " + button + " from " + section);
        logoutPage.logout();
        test.pass(button + " clicked successfully");
    }

    @Then("User should be logged out successfully")
    public void verifyLogoutSuccess() throws IOException 
    {
        try {
            Assert.assertTrue(logoutPage.isLogoutSuccessful(), "Logout failed");
            test.pass("User successfully logged out");
        } 
        catch (AssertionError e) 
        {
            String screenshotPath = screenshot();  // Capture screenshot on failure
            test.fail("Logout failed");
            test.addScreenCaptureFromPath(screenshotPath);  // Attach screenshot to Extent Report
        }
    }

    @Then("User should be redirected to the homepage")
    public void verifyRedirectionToHomepage() throws IOException 
    {
        try {
            Assert.assertTrue(logoutPage.isLogoutSuccessful(), "Redirection to homepage failed");
            test.pass("User successfully redirected to homepage");
        } 
        catch (AssertionError e) 
        {
            String screenshotPath = screenshot();  // Capture screenshot on failure
            test.fail("Redirection to homepage failed");
            test.addScreenCaptureFromPath(screenshotPath);
        }
    }
}

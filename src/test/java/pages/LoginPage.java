package pages;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;

public class LoginPage extends BaseClass 
{
    private WebDriver driver;
    private WebDriverWait wait;

    // **Constructor**
    public LoginPage(WebDriver driver) 
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // **Locators as Private WebElements**
    @FindBy(linkText = "Sign In")
    private WebElement signin;

    @FindBy(name = "username")
    private WebElement username;

    @FindBy(name = "password")
    private WebElement password;

    @FindBy(name = "signon")
    private WebElement loginBtn;

    @FindBy(xpath = "//ul[@class='messages']/li")
    private WebElement errorMsg;

    // **Getter Methods for WebElements**
    private WebElement getSignIn() { return wait.until(ExpectedConditions.elementToBeClickable(signin)); }
    private WebElement getUsername() { return wait.until(ExpectedConditions.visibilityOf(username)); }
    private WebElement getPassword() { return wait.until(ExpectedConditions.visibilityOf(password)); }
    private WebElement getLoginButton() { return wait.until(ExpectedConditions.elementToBeClickable(loginBtn)); }
    private WebElement getErrorMessage() { return wait.until(ExpectedConditions.visibilityOf(errorMsg)); }

    // **Method to Open Login Page**
    public void openLoginPage()
    {
        try {
            getSignIn().click();
            test.pass("Opened Login Page");
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Sign In link not found: " + e.getMessage());
        }
    }

    // **Method to Perform Login**
    public void login(String user, String pass) 
    {
        try {
            getUsername().sendKeys(user);
            getPassword().clear();
            getPassword().sendKeys(pass);
            test.pass("Entered credentials: " + user);
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Username or Password field not found: " + e.getMessage());
        }
    }

    // **Method to Click Login Button**
    public void submit() 
    {
        try 
        {
            getLoginButton().click();
            test.pass("Clicked on Login button");
            captureScreenshot("LoginAttempt");
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Login button not found: " + e.getMessage());
        }
    }

    // **Method to Check Error Message**
    public boolean isErrorMessageDisplayed()
    {
        try 
        {
            boolean isDisplayed = getErrorMessage().isDisplayed();
            test.pass("Error message displayed");
            return isDisplayed;
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Error message not found: " + e.getMessage());
            return false;
        }
    }

    // **Method to Verify Login Success**
    public boolean isLoginSuccessful()
    {
        boolean status = driver.getCurrentUrl().equals("https://petstore.octoperf.com/actions/Catalog.action");
        if (status) 
        {
            test.pass("Login successful, redirected to homepage");
        } 
        else 
        {
            test.fail("Login failed, incorrect URL");
        }
        return status;
    }

    // **Method to Capture Screenshot**
    public void captureScreenshot(String testName)
    {
        try {
            String screenshotPath = screenshot();
            test.addScreenCaptureFromPath(screenshotPath);
        } 
        catch (IOException e)
        {
            test.fail("Failed to capture screenshot: " + e.getMessage());
        }
    }
}

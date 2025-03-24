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

public class LogoutPage extends BaseClass 
{
    private WebDriverWait wait;

    // **Constructor**
    public LogoutPage(WebDriver driver) 
    {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // **Locators as Private WebElements**
    @FindBy(linkText = "Sign In")
    private WebElement signin;

    @FindBy(linkText = "Sign Out")
    private WebElement signout;

    @FindBy(name = "username")
    private WebElement username;

    @FindBy(name = "password")
    private WebElement password;

    @FindBy(name = "signon")
    private WebElement loginBtn;

    // **Getter Methods for WebElements**
    private WebElement getSignIn() { return wait.until(ExpectedConditions.elementToBeClickable(signin)); }
    private WebElement getSignOut() { return wait.until(ExpectedConditions.elementToBeClickable(signout)); }
    private WebElement getUsername() { return wait.until(ExpectedConditions.visibilityOf(username)); }
    private WebElement getPassword() { return wait.until(ExpectedConditions.visibilityOf(password)); }
    private WebElement getLoginButton() { return wait.until(ExpectedConditions.elementToBeClickable(loginBtn)); }

    // **Method to Log In**
    public void login(String user, String pass) 
    {
        try {
            getSignIn().click();
            getUsername().sendKeys(user);
            getPassword().clear();
            getPassword().sendKeys(pass);
            getLoginButton().click();
            test.pass("Logged in successfully as: " + user);
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Login elements not found: " + e.getMessage());
        }
    }

    // **Method to Log Out**
    public void logout()
    {
        try {
            getSignOut().click();
            test.pass("Clicked on Sign Out button");
            captureScreenshot("LogoutSuccessful");

            if (isLogoutSuccessful()) {
                test.pass("Logout successful, Sign In button is visible");
            } else {
                test.fail("Logout failed, Sign In button not found!");
            }
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Sign Out button not found: " + e.getMessage());
        }
    }

    // **Method to Verify Logout**
    public boolean isLogoutSuccessful()
    {
        try {
            return getSignIn().isDisplayed();
        } 
        catch (NoSuchElementException e) 
        {
            return false;
        }
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

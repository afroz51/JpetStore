package pages;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;

public class RegistrationPage extends BaseClass 
{
    WebDriver driver;
    WebDriverWait wait;

    public RegistrationPage(WebDriver driver) 
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
        PageFactory.initElements(driver, this);  // Initialize Page Factory
    }

    // Locators using @FindBy annotation
    @FindBy(linkText = "Sign In") 
    private WebElement signin;
    
    @FindBy(linkText = "Register Now!") 
    private WebElement registerBtn;
    
    @FindBy(name = "newAccount") 
    private WebElement submitBtn;
    
    @FindBy(xpath = "//h1") 
    private WebElement errorMsg;

    // User details locators
    @FindBy(name = "username") 
    private WebElement userid;
    
    @FindBy(name = "password") 
    private WebElement newpassword;
    
    @FindBy(name = "repeatedPassword") 
    private WebElement repeatpassword;
    
    @FindBy(name = "account.firstName") 
    private WebElement firstname;
    
    @FindBy(name = "account.lastName") 
    private WebElement lastname;
    
    @FindBy(name = "account.email") 
    private WebElement email;
    
    @FindBy(name = "account.phone") 
    private WebElement phone;
    
    @FindBy(name = "account.address1") 
    private WebElement address1;
    
    @FindBy(name = "account.address2") 
    private WebElement address2;
    
    @FindBy(name = "account.city") 
    private WebElement city;
    
    @FindBy(name = "account.state") 
    private WebElement state;
    
    @FindBy(name = "account.zip") 
    private WebElement zip;
    
    @FindBy(name = "account.country") 
    private WebElement country;
    
    @FindBy(name = "account.languagePreference") 
    private WebElement language;
    
    @FindBy(name = "account.favouriteCategoryId") 
    private WebElement favoriteCategory;
    
    @FindBy(name = "account.listOption") 
    private WebElement enableMyList;
    
    @FindBy(name = "account.bannerOption") 
    private WebElement enableMyBanner;

    // Method to Open Registration Page
    public void openRegistrationPage() 
    {
        try 
        {
            wait.until(ExpectedConditions.elementToBeClickable(signin)).click();
            wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
            test.pass("Navigated to Registration Page");
        } 
        catch (TimeoutException e) 
        {
            test.fail("Error opening Registration Page: " + e.getMessage());
        }
    }

    // Method to Fill Registration Form
    public void fillRegistrationForm(String id, String np, String rp, String fn, String ln, String em, String ph,
                                     String add1, String add2, String ci, String st, String zp, String co,
                                     String lang, String favCat, boolean listOption, boolean bannerOption) 
    {
        try 
        {
            wait.until(ExpectedConditions.visibilityOf(userid)).sendKeys(id);
            newpassword.sendKeys(np);
            repeatpassword.sendKeys(rp);
            firstname.sendKeys(fn);
            lastname.sendKeys(ln);
            email.sendKeys(em);
            phone.sendKeys(ph);
            address1.sendKeys(add1);
            address2.sendKeys(add2);
            city.sendKeys(ci);
            state.sendKeys(st);
            zip.sendKeys(zp);
            country.sendKeys(co);
            language.sendKeys(lang);
            favoriteCategory.sendKeys(favCat);

            if (listOption) 
            {
                enableMyList.click();
            }
            if (bannerOption) 
            {
                enableMyBanner.click();
            }
            test.pass("Filled Registration Form successfully");
        } 
        catch (TimeoutException e) 
        {
            test.fail("Error filling Registration Form: " + e.getMessage());
        }
    }

    // Method to Submit Registration Form
    public void submitForm() 
    {
        try 
        {
            wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
            captureScreenshot("RegistrationAttempt");
            test.pass("Submitted Registration Form");
        } 
        catch (TimeoutException e) 
        {
            test.fail("Error submitting form: " + e.getMessage());
        }
    }

    // Method to Check Error Message
    public boolean isErrorMessageDisplayed()
    {
        try 
        {
            boolean isDisplayed = wait.until(ExpectedConditions.visibilityOf(errorMsg)).isDisplayed();
            test.pass("Error message displayed");
            return isDisplayed;
        } 
        catch (TimeoutException e) 
        {
            test.fail("No error message displayed: " + e.getMessage());
            return false;
        }
    }

    // Method to Verify Registration Success
    public boolean isRegistrationSuccessful() 
    {
        boolean status = signin.isDisplayed();
        if (status) 
        {
            test.pass("Registration successful, redirected to homepage");
        } 
        else 
        {
            test.fail("Registration failed, incorrect URL");
        }
        return status;
    }

    // Method to Capture Screenshot
    public void captureScreenshot(String testName) 
    {
        try 
        {
            String screenshotPath = screenshot();
            test.addScreenCaptureFromPath(screenshotPath);
        } 
        catch (IOException e) 
        {
            test.fail("Failed to capture screenshot: " + e.getMessage());
        }
    }
}

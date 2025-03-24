package pages;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;

public class PetPage 
{
    WebDriver driver;
    WebDriverWait wait;

    public PetPage(WebDriver driver) 
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // **Locators using Page Factory**
    @FindBy(name = "keyword") 
    private WebElement searchBox;

    @FindBy(name = "searchProducts") 
    private WebElement searchButton;

    @FindBy(xpath = "//div[@id='Catalog']/table/tbody/tr/td/a") 
    private WebElement petLink;

    @FindBy(xpath = "//a[contains(text(),'Add to Cart')]") 
    private WebElement addToCartButton;

    @FindBy(xpath = "//table/tbody/tr[2]/td/a") 
    private WebElement cartItem;

    // **Search for a pet**
    public void searchPet(String petName) 
    {
        try 
        {
            wait.until(ExpectedConditions.visibilityOf(searchBox)).clear();
            searchBox.sendKeys(petName);
            searchButton.click();
        } 
        catch (NoSuchElementException e) 
        {
            System.out.println("Search box or button not found: " + e.getMessage());
        }
    }

    // **Select the first pet from search results (Using FluentWait)**
    public void selectPet()
    {
        try 
        {
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

            fluentWait.until(new Function<WebDriver, WebElement>() 
            {
                public WebElement apply(WebDriver driver) 
                {
                    return petLink;
                }
            }).click();
        } 
        catch (NoSuchElementException e)
        {
            System.out.println("Pet not found in search results: " + e.getMessage());
        }
    }

    // **Add selected pet to the cart (Using FluentWait)**
    public void addToCart() 
    {
        try 
        {
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

            fluentWait.until(new Function<WebDriver, WebElement>() 
            {
                public WebElement apply(WebDriver driver) 
                {
                    return addToCartButton;
                }
            }).click();
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Add to Cart button not found: " + e.getMessage());
        }
    }

    // **Verify if pet is added to the cart by checking for the added item**
    public boolean isPetAddedToCart() throws IOException 
    {
        try 
        {
            BaseClass.screenshot();

            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

            WebElement cartItemElement = fluentWait.until(new Function<WebDriver, WebElement>() 
            {
                public WebElement apply(WebDriver driver) 
                {
                    return cartItem;
                }
            });

            return cartItemElement.isDisplayed();
        } 
        catch (NoSuchElementException e) 
        {
            System.out.println("Pet not found in cart: " + e.getMessage());
            return false;
        }
    }
}

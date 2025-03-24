package pages;

import java.time.Duration;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.function.Function;

public class CartPage {
    WebDriver driver;
    WebDriverWait wait;

    // Locators for Cart Page using Page Factory
    @FindBy(name = "keyword") 
    private WebElement searchBox;

    @FindBy(name = "searchProducts") 
    private WebElement searchButton;

    @FindBy(xpath = "//div[@id='Catalog']/table/tbody/tr/td/a") 
    private WebElement petLink;

    @FindBy(xpath = "//a[contains(text(),'Add to Cart')]") 
    private WebElement addToCartButton;

    // Different paths for each product in the cart
    @FindBy(xpath = "//table/tbody/tr[2]/td/a") 
    private WebElement cartItem;

    @FindBy(xpath = "//table/tbody/tr[3]/td/a") 
    private WebElement cartItem2;

    @FindBy(xpath = "//table/tbody/tr[2]/td/a") 
    private WebElement cartItem3;

    @FindBy(xpath = "//table/tbody/tr[4]/td/a") 
    private WebElement cartItem4;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Search for a product
    public void searchProduct(String[] searchItems, int index) {
        try {
            if (index < 0 || index >= searchItems.length) {
                throw new IllegalArgumentException("Invalid index for search items array.");
            }

            String productName = searchItems[index].trim(); // Get product by index

            wait.until(ExpectedConditions.visibilityOf(searchBox)).clear();
            searchBox.sendKeys(productName);
            searchButton.click();
        } 
        catch (NoSuchElementException e) {
            System.out.println("Search box or button not found: " + e.getMessage());
        }
    }

    // Select the product link from search results (Using FluentWait)
    public void selectProduct() {
        try {
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

            WebElement product = fluentWait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return petLink;
                }
            });

            product.click();
        } 
        catch (NoSuchElementException e) {
            System.out.println("Product not found in search results: " + e.getMessage());
        }
    }

    // Add product to the cart (Using FluentWait)
    public void addProductToCart() {
        try {
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

            WebElement addToCart = fluentWait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return addToCartButton;
                }
            });

            addToCart.click();
        }
        catch (NoSuchElementException e) {
            System.out.println("Add to Cart button not found: " + e.getMessage());
        }
    }

    // Verify if product was successfully added to the cart
    public boolean isProductAddedToCart(String productNumber) {
        try {
            WebElement cartItemElement = null;
            switch (productNumber) {
                case "1":
                    cartItemElement = wait.until(ExpectedConditions.visibilityOf(cartItem));
                    break;
                case "2":
                    cartItemElement = wait.until(ExpectedConditions.visibilityOf(cartItem2));
                    break;
                case "3":
                    cartItemElement = wait.until(ExpectedConditions.visibilityOf(cartItem3));
                    break;
                case "4":
                    cartItemElement = wait.until(ExpectedConditions.visibilityOf(cartItem4));
                    break;
            }
            return cartItemElement.isDisplayed();
        } 
        catch (NoSuchElementException e) {
            System.out.println("Product not found in cart: " + e.getMessage());
            return false;
        }
    }

    // Clear the search box after each product is added
    public void clearSearchBox() {
        wait.until(ExpectedConditions.visibilityOf(searchBox)).clear();
    }
}

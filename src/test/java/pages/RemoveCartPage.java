package pages;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;

public class RemoveCartPage extends BaseClass {
    WebDriver driver;
    WebDriverWait wait;

    // Locators for Search and Cart Elements
    @FindBy(name = "keyword")
    private WebElement searchBox;

    @FindBy(name = "searchProducts")
    private WebElement searchButton;
    
    @FindBy(xpath = "//div[@id='Catalog']/table/tbody/tr/td/a") 
    private WebElement petLink;

    @FindBy(xpath = "//a[contains(text(),'Add to Cart')]")
    private WebElement addToCartButton;

    @FindBy(xpath = "//table/tbody/tr")
    private List<WebElement> cartTableRows;

    @FindBy(xpath = "//a[contains(text(),'Remove')]")
    private List<WebElement> removeButtons;

    @FindBy(xpath = "//form[@method='post']/table/tbody/tr/td/b[contains(text(),'Your cart is empty.')]")
    private WebElement emptyCartMessage;

    public RemoveCartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Search and Add Product to Cart
    public void searchAndAddToCart(String productName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(searchBox)).clear();
            searchBox.sendKeys(productName);
            searchButton.click();
            petLink.click();
            wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        } catch (NoSuchElementException e) {
            System.out.println("Product not found for adding to cart: " + e.getMessage());
        }
    }

    // Remove all products from the cart
    public boolean removeAllProductsFromCart() throws IOException {
        try {
            while (true) {
                // Refresh the remove buttons list
                List<WebElement> removeButtonsList = driver.findElements(By.xpath("//a[contains(text(),'Remove')]"));
                
                if (removeButtonsList.isEmpty()) {
                    break; // No more items to remove
                }

                WebElement removeButton = removeButtonsList.get(0);
                wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
                wait.until(ExpectedConditions.stalenessOf(removeButton)); // Wait until the item disappears
            }
            
            if(emptyCartMessage.isDisplayed())
            {
            	//Take screenshot
            	BaseClass.screenshot();
            }

            // Wait for the empty cart message to appear
            return wait.until(ExpectedConditions.visibilityOf(emptyCartMessage)).isDisplayed();
        } catch (NoSuchElementException e) {
            System.out.println("Error while removing products: " + e.getMessage());
            return false;
        }
    }

    // Check if the cart is empty
    public boolean isCartEmpty() throws IOException {
        try {
            return wait.until(ExpectedConditions.visibilityOf(emptyCartMessage)).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Full Test Workflow: Read from properties, add products, remove them, verify cart is empty
    public boolean testCartFunctionality() throws IOException {
        loadProperties();
        String items = prop.getProperty("searchItems");

        if (items == null || items.trim().isEmpty()) {
            throw new RuntimeException("No products found in properties file.");
        }

        String[] productNames = items.split(",");

        // Add products to the cart
        for (String product : productNames) {
            searchAndAddToCart(product.trim());
        }

        // Remove all products and check if the cart is empty
        return removeAllProductsFromCart();
    }
}

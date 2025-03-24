package tests;

import java.io.IOException;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import base.BaseClass;
import pages.CartPage;

public class CartTest extends BaseClass 
{
    CartPage cartPage;

    @BeforeMethod
    @Parameters({"browser", "url"})
    public void setUp(String browser, String url)
    {
        invokeBrowser(browser, url);
        cartPage = new CartPage(driver);
    }

    // Data Provider: Fetch from Properties File
    @DataProvider(name = "cartDataFromProperties")
    public Object[][] getCartDataFromProperties()
    {
        if (prop == null) 
        {
            loadProperties(); // Ensure properties file is loaded
        }

        // Fetch product names as a single comma-separated string from properties
        String items = prop.getProperty("searchItems");

        if (items == null || items.trim().isEmpty()) 
        {
            throw new RuntimeException("Product names are missing in data.properties.");
        }

        String[] searchItems = items.split(",");

        return new Object[][]{{searchItems}};
    }

    // Data Provider: Fetch from Excel File 
    @DataProvider(name = "cartDataFromExcel")
    public Object[][] getCartDataFromExcel()
    {
        if (getWorkbook() == null) 
        {
            loadExcelData("C:\\Users\\AFROZ\\Downloads\\data.xlsx");
        }

        if (getSheet() == null)
        {
            setSheet("userdata");
        }

        String items = getCellData(1, 17);  

        System.out.println("Raw Excel Data: [" + items + "]");

        if (items == null || items.trim().isEmpty())
        {
            throw new RuntimeException("Product names are missing in Excel.");
        }

        // Ensure proper splitting
        String[] searchItems = items.split("\\s*,\\s*");  // Splitting by commas with spaces

        // Debug: Print out split values
        System.out.println("Parsed Product List:");
        for (String product : searchItems)
        {
            System.out.println("- " + product);
        }

        return new Object[][] { { searchItems } };  // Return as an array
    }

    // Test 1: Fetch data from Properties File
    @Test(dataProvider = "cartDataFromProperties", enabled = false)
    public void testAddMultipleProductsToCart_PropertiesFile(String[] searchItems) throws IOException 
    {
        executeAddToCartTest(searchItems, "Properties File");
    }

    // Test 2: Fetch data from Excel File
    @Test(dataProvider = "cartDataFromExcel", enabled = true)
    public void testAddMultipleProductsToCart_ExcelFile(String[] searchItems) throws IOException 
    {
        executeAddToCartTest(searchItems, "Excel File");
    }

    // Reusable Test Execution Method
    private void executeAddToCartTest(String[] searchItems, String dataSource) throws IOException 
    {
        try {
            test = extent.createTest("Adding Multiple Products to Cart (" + dataSource + ")");

            for (int i = 0; i < searchItems.length; i++) {
                cartPage.searchProduct(searchItems, i); // Updated method call
                cartPage.selectProduct();
                cartPage.addProductToCart();
                Assert.assertTrue(cartPage.isProductAddedToCart(String.valueOf(i + 1)), 
                                  "Product " + (i + 1) + " was not added to the cart!");

                // Clear search box before next search
                if (i < searchItems.length - 1) {
                    cartPage.clearSearchBox();
                }
            }

            // Take Screenshot
            screenshot();
            test.pass("All products added to the cart successfully.");
        } 
        catch (NoSuchElementException e)
        {
            handleTestFailure(e);
        }
    }

    // Reusable Failure Handling Method
    private void handleTestFailure(NoSuchElementException e) 
    {
        test.fail("Test failed due to missing element: " + e.getMessage());
        try {
            String screenshotPath = screenshot();
            test.addScreenCaptureFromPath(screenshotPath);
        } 
        catch (IOException ioException)
        {
            test.fail("Failed to capture screenshot: " + ioException.getMessage());
        }
        Assert.fail("Test failed due to missing element.");
    }

    @AfterMethod
    public void close()
    {
        closeBrowser();
        flushExtentReport();
    }
}

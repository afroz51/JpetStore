package tests;

import java.io.IOException;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import base.BaseClass;
import pages.RemoveCartPage;

public class RemoveCartTest extends BaseClass {
    RemoveCartPage removeCartPage;

    @BeforeMethod
    @Parameters({"browser", "url"})
    public void setUp(String browser, String url) {
        invokeBrowser(browser, url);
        removeCartPage = new RemoveCartPage(driver);
    }

    // Test: Add and Remove Multiple Products from Cart (Using Properties File)
    @Test
    public void testRemoveProducts() throws IOException {
        try {
            test = extent.createTest("Removing Products from Cart (Properties File)");

            // Execute full cart test (add & remove)
            boolean result = removeCartPage.testCartFunctionality();

            // Final Check: Cart Should Be Empty
            Assert.assertTrue(result, "Cart is not empty after all removals!");

            test.pass("All products removed from the cart successfully. Cart is empty.");
        } catch (NoSuchElementException e) {
            handleTestFailure(e);
        }
    }

    // Reusable Failure Handling Method
    private void handleTestFailure(NoSuchElementException e) {
        test.fail("Test failed due to missing element: " + e.getMessage());
        try {
            String screenshotPath = screenshot();
            test.addScreenCaptureFromPath(screenshotPath);
        } catch (IOException ioException) {
            test.fail("Failed to capture screenshot: " + ioException.getMessage());
        }
        Assert.fail("Test failed due to missing element.");
    }

    @AfterMethod
    public void close() {
        closeBrowser();
        flushExtentReport();
    }
}

package tests;

import java.io.IOException;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import base.BaseClass;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import pages.CheckoutPageValid;

public class CheckoutTestValid extends BaseClass {
    CheckoutPageValid checkoutPage;

    @BeforeMethod
    @Parameters({"browser", "url"})
    public void setUp(String browser, String url) throws IOException {
        invokeBrowser(browser, url);
        loadProperties(); // Load data from properties file
        checkoutPage = new CheckoutPageValid(driver);
    }

    // **Test: Checkout Process with Valid Data**
    @Test(enabled = true)
    @Description("Test checkout process with valid data handling from properties file")
    public void testCheckout_ValidData() throws IOException, InterruptedException {
        executeCheckoutTest();
    }

    // **Reusable Test Execution Method**
    @Step("Executing checkout process with valid data")
    private void executeCheckoutTest() throws IOException, InterruptedException {
        try {
            test = extent.createTest("Checkout Process with Valid Data");
            Allure.step("Test Execution Started: Checkout Process with Valid Data");

            checkoutPage.signIn(prop.getProperty("userid"), prop.getProperty("newpassword"));
            checkoutPage.addProductToCart(prop.getProperty("searchProduct"));
            checkoutPage.proceedToCheckout();

            checkoutPage.fillCheckoutDetails(
                prop.getProperty("firstName"),
                prop.getProperty("lastName"),
                prop.getProperty("address1"),
                prop.getProperty("address2"),
                prop.getProperty("city"),
                prop.getProperty("state"),
                prop.getProperty("zip"),
                prop.getProperty("country"),
                prop.getProperty("CardType"),
                prop.getProperty("CardNumber"),
                prop.getProperty("ExpiryDate")
            );

            checkoutPage.completeCheckout();

            if (checkoutPage.isCheckoutSuccessMessageDisplayed()) {
                screenshot();
                test.pass("Order placed successfully.");
                Allure.step("Order placed successfully.");
            } else {
                Assert.fail("Success message not displayed!");
            }
        } catch (NoSuchElementException e) {
            handleTestFailure(e);
        }
    }

    // **Failure Handling Method**
    @Step("Handling Test Failure")
    private void handleTestFailure(NoSuchElementException e) {
        test.fail("Test failed due to missing element: " + e.getMessage());
        Allure.step("Test failed due to missing element: " + e.getMessage());

        try {
            String screenshotPath = screenshot();
            test.addScreenCaptureFromPath(screenshotPath);
            Allure.addAttachment("Screenshot on Failure", "image/png", screenshotPath);
        } catch (IOException ioException) {
            test.fail("Failed to capture screenshot: " + ioException.getMessage());
            Allure.step("Failed to capture screenshot: " + ioException.getMessage());
        }

        Assert.fail("Test failed due to missing element.");
    }

    @AfterMethod
    public void close() {
        closeBrowser();
        flushExtentReport();
        Allure.step("Browser closed and Extent Report flushed.");
    }
}

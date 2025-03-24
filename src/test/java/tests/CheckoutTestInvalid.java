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
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import pages.CheckoutPageInvalid;

public class CheckoutTestInvalid extends BaseClass {
    CheckoutPageInvalid checkoutPage;

    @BeforeMethod
    @Parameters({"browser", "url"})
    public void setUp(String browser, String url) throws IOException {
        invokeBrowser(browser, url);
        checkoutPage = new CheckoutPageInvalid(driver);
    }

    // **Data Provider: Fetch Checkout Data from Excel**
    @DataProvider(name = "checkoutDataFromExcel")
    public Object[][] getCheckoutDataFromExcel() {
        if (getWorkbook() == null) {
            loadExcelData("C:\\Users\\AFROZ\\Downloads\\data.xlsx");
        }

        if (getSheet() == null) {
            setSheet("userdata");  // Ensure correct sheet is selected
        }

        String firstName = getCellData(1, 2);
        String lastName = getCellData(1, 3);
        String address1 = getCellData(1, 6);
        String address2 = getCellData(1, 7);
        String city = getCellData(1, 8);
        String state = getCellData(1, 9);
        String zipCode = getCellData(1, 10);
        String country = getCellData(1, 11);
        String cardType = getCellData(1, 18);
        String cardNumber = getCellData(1, 19);
        String expiryDate = getCellData(1, 20);

        if (firstName == null || lastName == null || address1 == null || address2 == null || city == null || 
            state == null || zipCode == null || country == null || cardType == null || 
            cardNumber == null || expiryDate == null) {
            throw new RuntimeException("One or more checkout details are missing in Excel.");
        }

        return new Object[][]{{firstName, lastName, address1, address2, city, state, zipCode, country, cardType, cardNumber, expiryDate}};
    }

    // **Test: Checkout with Invalid Data Handling**
    @Test(dataProvider = "checkoutDataFromExcel", enabled = true)
    @Description("Test checkout process with invalid data handling from Excel")
    public void testCheckout_InvalidData(String firstName, String lastName, String address1, String address2, 
                                         String city, String state, String zipCode, String country, 
                                         String cardType, String cardNumber, String expiryDate) throws IOException, InterruptedException {
        executeCheckoutTest(firstName, lastName, address1, address2, city, state, zipCode, country, cardType, cardNumber, expiryDate);
    }

    // **Reusable Test Execution Method**
    @Step("Executing checkout process with invalid data")
    private void executeCheckoutTest(String firstName, String lastName, String address1, String address2, 
                                     String city, String state, String zipCode, String country, 
                                     String cardType, String cardNumber, String expiryDate) throws IOException, InterruptedException {
        try {
            test = extent.createTest("Checkout Process with Invalid Data Handling");
            Allure.step("Test Execution Started: Checkout Process with Invalid Data Handling");

            checkoutPage.signIn(getCellData(1, 21), getCellData(1, 22));  // Fetch UserID and Password from Excel
            checkoutPage.addProductToCart(getCellData(1, 16));  // Fetch product name from Excel
            checkoutPage.proceedToCheckout();
            
            checkoutPage.fillCheckoutDetails(firstName, lastName, address1, address2, city, state, zipCode, country, cardType, cardNumber, expiryDate);
            checkoutPage.completeCheckout();

            if (checkoutPage.isErrorMessageDisplayed()) {
            	screenshot();
                test.pass("Invalid data error message displayed as expected.");
                Allure.step("Invalid data error message displayed as expected.");
            } else {
                Assert.fail("No error message displayed for invalid data!");
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

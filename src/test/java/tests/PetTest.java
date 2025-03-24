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
import pages.PetPage;

public class PetTest extends BaseClass {
    PetPage petPage;

    @BeforeMethod
    @Parameters({"browser", "url"})
    public void setUp(String browser, String url) {
        invokeBrowser(browser, url);
        petPage = new PetPage(driver);
    }

    // **Data Provider: Fetch from Properties File**
    @DataProvider(name = "petDataFromProperties")
    public Object[][] getPetDataFromProperties() {
        if (prop == null) {
            loadProperties(); // Ensure properties file is loaded
        }

        String petName = prop.getProperty("searchProduct");
        if (petName == null || petName.isEmpty()) 
        {
            throw new RuntimeException("Property 'searchProduct' is missing in data.properties.");
        }

        return new Object[][]{{petName}};
    }

    // **Data Provider: Fetch from Excel File (Row: 1, Column: 17)**
    @DataProvider(name = "petDataFromExcel")
    public Object[][] getPetDataFromExcel() {
        // Ensure Excel data is loaded
        if (getWorkbook() == null) {
            loadExcelData("C:\\Users\\AFROZ\\Downloads\\data.xlsx");
        }

        // Ensure the sheet is set
        if (getSheet() == null) {
            setSheet("userdata");
        }

        // Fetch data from Excel
        int row = 1, col = 16; // Excel data location
        String petName = getCellData(row, col);

        // Validate data
        if (petName == null || petName.isEmpty()) {
            throw new RuntimeException("No data found in Excel at row " + row + ", column " + col);
        }

        return new Object[][]{{petName}};
    }

    // **Test 1: Fetch data from Properties File**
    @Test(dataProvider = "petDataFromProperties", enabled = false)
    @Description("Test search and add to cart using data from properties file")
    public void testSearchAndAddToCart_PropertiesFile(String petName) throws IOException {
        executeSearchAndAddToCartTest(petName, "Properties File");
    }

    // **Test 2: Fetch data from Excel File**
    @Test(dataProvider = "petDataFromExcel", enabled = true)
    @Description("Test search and add to cart using data from Excel file")
    public void testSearchAndAddToCart_ExcelFile(String petName) throws IOException {
        executeSearchAndAddToCartTest(petName, "Excel File");
    }

    // **Reusable Test Execution Method with Allure Steps**
    @Step("Executing search and add to cart test for {petName} from {dataSource}")
    private void executeSearchAndAddToCartTest(String petName, String dataSource) throws IOException {
        try {
            test = extent.createTest("Searching and Adding Pet (" + dataSource + "): " + petName);
            Allure.step("Test Execution Started: Searching and Adding Pet (" + dataSource + "): " + petName);

            petPage.searchPet(petName);
            petPage.selectPet();
            petPage.addToCart();

            Assert.assertTrue(petPage.isPetAddedToCart(), "Pet was not added to the cart!");
            test.pass("Pet added to cart successfully.");
            Allure.step("Pet added to cart successfully.");
        } catch (NoSuchElementException e) {
            handleTestFailure(e);
        }
    }

    // **Reusable Failure Handling Method with Allure Reporting**
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

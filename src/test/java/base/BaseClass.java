package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import excel.ExcelUtility;

public class BaseClass extends ExcelUtility
{
    public static WebDriver driver;
    public static Properties prop;
    public static ExtentReports extent;
    public static ExtentTest test;
    private static String extentReportPath;

    // Initialize Extent Reports only if not already initialized
    public static void initExtentReport()
    {
        if (extent == null) 
        {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            extentReportPath = System.getProperty("user.dir") + "/target/ExtentReports/ExtentReport_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(extentReportPath);
            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Browser", "Chrome"); // Default value, updated dynamically
        }
    }

    // Initialize WebDriver and Properties
    public static void invokeBrowser()
    {
        initExtentReport();  // Ensure Extent Report is initialized only once

        if (prop == null) 
        {
            loadProperties();
        }
        
        if (driver == null) 
        {
            String browser = prop.getProperty("browser");

            switch (browser.toLowerCase()) 
            {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                default:
                    throw new RuntimeException("Invalid browser name in data.properties");
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get(prop.getProperty("url"));
        }
    }
    
    // Overloaded method for specific browser and URL
    public static void invokeBrowser(String browser, String url)
    {
        initExtentReport();

        if (driver == null) 
        {
            switch (browser.toLowerCase()) 
            {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                default:
                    throw new RuntimeException("Invalid browser name: " + browser);
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get(url);
        }
    }

    // Load data from properties file
    public static void loadProperties()
    {
        prop = new Properties();
        try (FileInputStream ip = new FileInputStream("src/test/java/data/data.properties")) 
        {
            prop.load(ip);
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    // Load data from Excel file (if not already loaded)
    public static void loadExcelData()
    {
        try 
        {
            if (!isExcelDataLoaded()) 
            {
                ExcelUtility.loadExcelData("C:\\Users\\AFROZ\\Downloads\\data.xlsx");
                setSheet("userdata");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    // Check if Excel data is already loaded
    private static boolean isExcelDataLoaded() 
    {
        return ExcelUtility.getWorkbook() != null;
    }

    // Capture Screenshot and return file path
    public static String screenshot() throws IOException 
    {
        new WebDriverWait(driver, Duration.ofSeconds(3)) // Wait for UI to stabilize before taking screenshot
            .until(d -> ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE) != null);

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotPath = System.getProperty("user.dir") + "/screenshots/" + "page_" + System.currentTimeMillis() + ".png";
        FileUtils.copyFile(src, new File(screenshotPath));
        
        return screenshotPath; // Return full path of screenshot
    }

    // Close Browser & Flush Reports
    public static void closeBrowser()
    {
        if (driver != null) 
        {
            driver.quit();
            driver = null; // Prevent re-use of a closed driver
        }
    }
    
    // Flush Extent Report only if initialized
    public static void flushExtentReport() 
    {
        if (extent != null) 
        {
            extent.flush();
        }
    }
    
    @BeforeSuite
    public void clearAllureResults() {
    	String[] allurePaths = {"allure-results", "/allure-results/"};

        for (String path : allurePaths) {
            File allureResults = new File(path);
            if (allureResults.exists() && allureResults.isDirectory()) {
                for (File file : allureResults.listFiles()) {
                    file.delete();
                }
            }
        }
    }

    // Generate Allure Report after test execution (Runs in Background)
    @AfterSuite
    public void generateAllureReport() 
    {
        flushExtentReport(); // Ensure Extent Reports are saved first
        
        try 
        {
            ProcessBuilder reportBuilder = new ProcessBuilder("cmd.exe", "/c", "mvn allure:report");
            reportBuilder.redirectErrorStream(true);
            Process reportProcess = reportBuilder.start();
            reportProcess.waitFor(); // Wait until report is generated

            String reportPath = System.getProperty("user.dir") + "/allure-results/";
            File reportDir = new File(reportPath);

            if (reportDir.exists() && reportDir.isDirectory()) 
            {
                System.out.println("Allure Report generated successfully in: " + reportPath);
            } 
            else 
            {
                System.out.println("Allure Report generation failed. Check Maven logs.");
            }

        } 
        catch (IOException | InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
}

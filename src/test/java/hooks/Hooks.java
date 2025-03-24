package hooks;

import base.BaseClass;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks extends BaseClass
{

    @Before
    public void setUp() 
    {
        initExtentReport();  // Initialize Extent Report
        invokeBrowser();     // Launch Browser
    }

    @After
    public void tearDown() 
    {
        closeBrowser();      // Close Browser
        flushExtentReport(); // Flush Extent Report
    }
}

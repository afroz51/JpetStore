package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/java/features/Logout.feature",  // Path to Logout feature file
    glue = {"stepdef", "hooks"},                         // Path to step definitions and hooks package
    tags = "@Logout",                                    // Run scenarios tagged with @Logout
    plugin = {
        "pretty",                                        // Display readable console output
        "html:target/cucumber-reports/logout-report.html",  // Generate HTML report
        "json:target/cucumber-reports/logout.json"       // Generate JSON report
    }
)
public class LogoutRunner extends AbstractTestNGCucumberTests {
    // Runs Cucumber tests with TestNG support
}

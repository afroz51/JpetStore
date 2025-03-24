package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/java/features/Login.feature", 
    glue = {"stepdef", "hooks"}, 
    tags = "@valid1 or @invalid1 or @valid2 or @invalid2", 
    plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json"} 
)
public class LoginRunner extends AbstractTestNGCucumberTests {
    // Runs Cucumber tests with TestNG support
}

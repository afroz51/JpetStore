package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class) // Enables JUnit execution for Cucumber tests
@CucumberOptions(
    features = "src/test/java/features/Register.feature", 
    glue = {"stepdef", "hooks"}, 
    tags = "@valid1 or @invalid1 or @valid2 or @invalid2", 
    plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json"}
)
public class RegisterRunner 
{
    // Runs Cucumber tests with JUnit support
}

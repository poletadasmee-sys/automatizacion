package Runner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/ElectroHogar.feature",
        glue = {"StepDefinition"},
        plugin = {"pretty", "html:target/cucumber-report.html"},
        monochrome = true,
        tags = "@Sodimac"
)

public class Runner {
    public static void main(String[] args) {
        JUnitCore.runClasses(Runner.class);
    }
}
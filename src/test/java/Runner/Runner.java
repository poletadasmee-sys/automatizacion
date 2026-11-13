package Runner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/Pisos.feature",
        glue = {"StepDefinition"},
        plugin = {"pretty", "html:target/cucumber-report.html"},
        monochrome = true
)

public class Runner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(Runner.class);
        if (!result.wasSuccessful()) {
            for (Failure failure : result.getFailures()) {
                System.err.println(failure.getDescription());
                failure.getException().printStackTrace(System.err);
            }
            System.exit(1);
        }
    }
}

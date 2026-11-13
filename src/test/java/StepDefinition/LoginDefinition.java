package StepDefinition;

import ObjectPage.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Control.DriverContext;
import org.openqa.selenium.WebDriver;

public class LoginDefinition {

    private LoginPage loginPage = new LoginPage();
    private WebDriver driver = DriverContext.getDriver();

   /* @Given("que ingreso a la página principal de Sodimac Chile {string}")
    public void queIngresoALaPaginaPrincipalDeSodimacChile(String url) {
        // Como 'navegarA' lo tienes en esta misma clase, puedes llamarlo directo o usar driver.get(url)
        driver.get(url);
    }

    @When("ingreso credenciales {string} y {string}")
    public void ingresoCredencialesY(String email, String password) {
        loginPage.ingresarCredenciales(email, password);
    }

    @Then("verifico el mensaje {string}")
    public void verificoElMensaje(String mensaje) {
        loginPage.verificarMensaje(mensaje);
    }
    */

}
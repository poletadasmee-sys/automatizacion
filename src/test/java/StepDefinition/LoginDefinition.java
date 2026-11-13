package StepDefinition;

import ObjectPage.HomePage;
import ObjectPage.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
//import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class LoginDefinition {
    private LoginPage loginPage = new LoginPage();

    @Given("que ingreso a la página principal de Sodimac Chile {string}")
    public void queIngresoALaPaginaPrincipalDeSodimacChile(String url) {
 //       loginPage.navegarA(url);
    }

    @When("ingreso credenciales {string} y {string}")
    public void ingresoCredencialesY(String email, String password) {
        // loginPage.ingresarCredenciales(email, password);
    }

    @Then("verifico el mensaje {string}")
  public void verificoElMensaje(String mensaje) {
        // loginPage.verificarMensaje(mensaje);  loginPage.verificarMensaje(mensaje);
    }

}
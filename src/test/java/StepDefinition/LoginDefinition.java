package StepDefinition;

import ObjectPage.LoginPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginDefinition {
    private LoginPage loginPage;

    @When("ingreso credenciales {string} y {string}")
    public void ingresoCredencialesY(String email, String password) {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        loginPage.ingresarCredenciales(email, password);
    }

    @Then("verifico el mensaje {string}")
    public void verificoElMensaje(String mensaje) {
        loginPage.verificarMensaje(mensaje);
    }
}
package StepDefinition;

import ObjectPage.HomePage;
import ObjectPage.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginDefinition {
    private final LoginPage loginPage = new LoginPage();
    private final HomePage homePage = new HomePage();

    @Given("que ingreso a la página principal de Sodimac Chile {string}")
    public void queIngresoALaPaginaPrincipalDeSodimacChile(String url) {
        loginPage.navegar(url);
    }

    @When("ingreso credenciales {string} y {string}")
    public void ingresoCredencialesY(String email, String password) {
        loginPage.ingresarCredenciales(email, password);
    }

    @Then("verifico el mensaje {string}")
    public void verificoElMensaje(String mensaje) {
        loginPage.verificarMensaje(mensaje);
    }

    @Given("abrir el navegador en la url {string}")
    public void abrirNavegador(String url) {
        loginPage.navegar(url);
    }

    @And("ingresar el usuario {string}")
    public void ingresarElUsuario(String usuario) {
        loginPage.escribirUsersname(usuario);
    }

    @And("ingresar la pass {string}")
    public void ingresarLaPass(String password) {
        loginPage.escribirPassword(password);
    }

    @When("presiono el boton Submit")
    public void presionoElBotonSubmit() {
        loginPage.clickBtnSubmit();
    }

    @Then("se valida el mensaje Logged In Successfully")
    public void seValidaElMensajeLoggedInSuccessfully() {
        homePage.validarMsgBienvenida();
    }

    @Then("se valida el mensaje Your username is invalid!")
    public void seValidaElMensajeUsuarioInvalido() {
        loginPage.validarMsgUsuarioInvalido();
    }
}
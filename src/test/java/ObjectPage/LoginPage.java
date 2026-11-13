package ObjectPage;

import Constant.Navegador;
import Control.BaseController;
import Control.DriverContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.fail;

public class LoginPage extends BaseController {
    @FindBy(id = "username")
    private WebElement nombreUsuario;

    @FindBy(css = "input[name='password']")
    private WebElement passwordUsuario;

    @FindBy(xpath = "//button[contains(text(),'Submit')]")
    private WebElement btnSubmit;

    @FindBy(xpath = "//*[text() = 'Your username is invalid!']")
    private WebElement msgUsuarioInvalido;

    public void navegar(String url) {
        DriverContext.setUp(Navegador.Chrome, url);
        sincronizarDriver();
    }

    public void ingresarCredenciales(String email, String password) {
        sincronizarDriver();
        try {
            if (visualizarElemento(this.nombreUsuario, 10)) {
                this.nombreUsuario.clear();
                this.nombreUsuario.sendKeys(email);
            }
            if (visualizarElemento(this.passwordUsuario, 10)) {
                this.passwordUsuario.clear();
                this.passwordUsuario.sendKeys(password);
            }
        } catch (Exception e) {
            fail("Error al ingresar credenciales: " + e.getMessage());
        }
    }

    public void escribirUsersname(String nUsuario) {
        sincronizarDriver();
        try {
            if (visualizarElemento(this.nombreUsuario, 10)) {
                this.nombreUsuario.clear();
                this.nombreUsuario.sendKeys(nUsuario);
            } else {
                fail("No se encuentra el campo Nombre de Usuario en la página");
            }
        } catch (Exception e) {
            fail("Error al escribir en el campo de usuario: " + e.getMessage());
        }
    }

    public void escribirPassword(String pass) {
        sincronizarDriver();
        try {
            this.passwordUsuario.clear();
            this.passwordUsuario.sendKeys(pass);
        } catch (Exception e) {
            fail("Error al escribir en el campo de password: " + e.getMessage());
        }
    }

    public void clickBtnSubmit() {
        sincronizarDriver();
        try {
            this.btnSubmit.click();
        } catch (Exception e) {
            fail("Error al hacer click en el botón Submit: " + e.getMessage());
        }
    }

    public void verificarMensaje(String mensaje) {
        sincronizarDriver();
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(15));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(normalize-space(.), '" + mensaje + "')]")));
            System.out.println("Mensaje visible: " + mensaje);
        } catch (Exception e) {
            fail("Error al detectar el mensaje esperado: " + mensaje + " -> " + e.getMessage());
        }
    }

    public void validarMsgUsuarioInvalido() {
        sincronizarDriver();
        try {
            if (this.msgUsuarioInvalido != null && this.msgUsuarioInvalido.isDisplayed()) {
                System.out.println("Mensaje de usuario inválido visible");
            } else {
                System.out.println("Error al visualizar el mensaje de usuario inválido!");
            }
        } catch (Exception e) {
            fail("Error al detectar el mensaje de usuario inválido: " + e.getMessage());
        }
    }

}

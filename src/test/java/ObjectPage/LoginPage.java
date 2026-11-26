package ObjectPage;

import Control.BaseController;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import Constant.Constant;
import Control.DriverContext;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage extends BaseController {
    private WebDriver driver;

    @FindBy(xpath = "//*[normalize-space()='Iniciar sesión']")
    private WebElement inicioSesion;

    @FindBy(xpath = "//*[@id=\"testId-cc-login-form-email-input\"]")
    private WebElement ingresoCorreo;

    @FindBy(xpath = "//*[@id=\"testId-cc-login-form-password-input\"]")
    private WebElement ingresoPassword;

    @FindBy(xpath ="//*[@id=\"testId-cc-login-form-submit\"]")
    private WebElement btnIngresar;

    @FindBy(xpath = "//*[contains(text(),'Correo electrónico o contraseña incorrecta')]")
    private WebElement msgUsuarioInvalido;

    private final WebDriverWait wait;

    public LoginPage() {
        this.driver = DriverContext.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver no está inicializado.");
        }
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.TIME_RESPONSE));
    }

    public void clickIniciarSesion() {
        clickConFallback(inicioSesion);
    }

    private void clickConFallback(WebElement elemento) {
        WebElement boton = wait.until(ExpectedConditions.elementToBeClickable(elemento));
        try {
            boton.click();
        } catch (ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", boton);
        }
    }

    public void ingresarCredenciales(String email, String password) {
        clickIniciarSesion();
        wait.until(ExpectedConditions.visibilityOf(ingresoCorreo)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOf(ingresoPassword)).sendKeys(password);
        pausarParaVisualizarEscritura();
        clickBtnIngresar();
    }

    private void pausarParaVisualizarEscritura() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Se interrumpió la pausa para visualizar la contraseña.", e);
        }
    }

    public void clickBtnIngresar() {
        clickConFallback(btnIngresar);
    }

    public void verificarMensaje(String mensaje) {
        String mensajeVisible = wait.until(
                ExpectedConditions.visibilityOf(msgUsuarioInvalido)
        ).getText().trim();

        org.junit.Assert.assertEquals(mensaje, mensajeVisible);
    }
}
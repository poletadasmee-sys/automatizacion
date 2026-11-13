package ObjectPage;

import Control.BaseController;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import Constant.Constant;
import Control.DriverContext;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.Assert.fail;

public class LoginPage extends BaseController {
    private WebDriver driver;

    @FindBy(xpath = "//*[@id=\"user-info\"]/div/div[1]/p")
    private WebElement inicioSesion;

    @FindBy(xpath ="//*[@id=\"testId-cc-modal\"]/div/div/div[2]")
    private WebElement despliegueInicioSesion;

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

    public void ingresarCredenciales(String email, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(inicioSesion)).click();
        wait.until(ExpectedConditions.visibilityOf(despliegueInicioSesion));
        wait.until(ExpectedConditions.visibilityOf(ingresoCorreo)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOf(ingresoPassword)).sendKeys(password);
        clickBtnIngresar();
    }

    public void clickBtnIngresar() {
        wait.until(ExpectedConditions.elementToBeClickable(btnIngresar)).click();
    }

    public void verificarMensaje(String mensaje) {
        String mensajeVisible = wait.until(
                ExpectedConditions.visibilityOf(msgUsuarioInvalido)
        ).getText().trim();

        org.junit.Assert.assertEquals(mensaje, mensajeVisible);
    }
}
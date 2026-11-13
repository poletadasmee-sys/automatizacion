package Control;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Constant.Constant;

import java.time.Duration;

public class BaseController {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BaseController() {
        sincronizarDriver();
    }

    protected void sincronizarDriver() {
        this.driver = DriverContext.getDriver();
        if (this.driver != null) {
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.TIME_DISPLAY), Duration.ofSeconds(Constant.POLLING));
            PageFactory.initElements(new AjaxElementLocatorFactory(this.driver, Constant.TIME_RESPONSE), this);
        } else {
            System.out.println("WebDriver no está inicializado!");
        }
    }

    public boolean visualizarElemento(WebElement elementoWeb, int tiempoEspera) {
        try {
            WebDriverWait wait2 = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(tiempoEspera));
            wait2.until(ExpectedConditions.visibilityOf(elementoWeb));
            System.out.println("Es visible el elemento web " + elementoWeb.getText());
            return true;
        } catch (Exception var3) {
            System.out.println("No es visible el elemento web" + elementoWeb);
            return false;
        }
    }

}

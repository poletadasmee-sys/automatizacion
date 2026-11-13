package Control;

import Constant.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Normalizer;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

/**
 * Clase base para los Page Objects nuevos.
 *
 * Extiende {@link BaseController} (no lo modifica) y centraliza:
 * - el acceso al driver compartido ({@link DriverContext}),
 * - las esperas explícitas (WebDriverWait + ExpectedConditions),
 * - acciones comunes (click con fallback JS, escribir, leer texto),
 * - consultas booleanas para que los steps hagan las aserciones.
 *
 * Reglas: los locators son {@code private static final By} en cada página;
 * nada de Thread.sleep; las páginas no hacen asserts.
 */
public abstract class BasePage extends BaseController {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        super();
        this.driver = DriverContext.getDriver();
        if (this.driver == null) {
            throw new IllegalStateException(
                    "WebDriver no está inicializado. Ejecuta primero el step que abre el navegador.");
        }
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(Constant.TIME_RESPONSE));
    }

    // ---------------------------------------------------------------- esperas

    protected WebElement esperarVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement esperarClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Igual que esperarClickable(By) pero con un timeout propio (para elementos opcionales). Lanza TimeoutException. */
    protected WebElement esperarClickable(By locator, int segundos) {
        return new WebDriverWait(driver, Duration.ofSeconds(segundos))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement esperarPresente(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected List<WebElement> esperarTodosVisibles(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void esperarInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void esperarUrlContiene(String fragmento) {
        wait.until(ExpectedConditions.urlContains(fragmento));
    }

    // ---------------------------------------------------------------- acciones

    /** Click esperando que sea clickable; si otro elemento lo tapa, usa click por JavaScript. */
    protected void click(By locator) {
        WebElement elemento = esperarClickable(locator);
        try {
            elemento.click();
        } catch (ElementClickInterceptedException e) {
            clickPorJs(elemento);
        }
    }

    /** Click en un elemento opcional (cookies, modales). Devuelve true si estaba visible y se hizo click. */
    protected boolean clickSiVisible(By locator, int segundos) {
        try {
            WebElement elemento = new WebDriverWait(driver, Duration.ofSeconds(segundos))
                    .until(ExpectedConditions.elementToBeClickable(locator));
            try {
                elemento.click();
            } catch (ElementClickInterceptedException e) {
                clickPorJs(elemento);
            }
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected void escribir(By locator, String texto) {
        WebElement campo = esperarVisible(locator);
        campo.clear();
        campo.sendKeys(texto);
    }

    protected void clickPorJs(WebElement elemento) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
    }

    protected void scrollHasta(WebElement elemento) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", elemento);
    }

    // ---------------------------------------------------------------- consultas

    protected String obtenerTexto(By locator) {
        return esperarVisible(locator).getText().trim();
    }

    /** true si el elemento se vuelve visible dentro del tiempo indicado; nunca lanza TimeoutException. */
    protected boolean estaVisible(By locator, int segundos) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(segundos))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean estaVisible(By locator) {
        return estaVisible(locator, Constant.TIME_RESPONSE);
    }

    /** true si existe al menos un elemento en el DOM (sin esperar). */
    protected boolean existe(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    // ---------------------------------------------------------------- utilidades

    /** Minúsculas, sin tildes y sin espacios extremos: para comparar textos de la UI. */
    protected static String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}

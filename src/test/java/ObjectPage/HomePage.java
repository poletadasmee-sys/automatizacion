
package ObjectPage;

import Control.BaseController;
import Control.DriverContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BaseController {

    public void cerrarAsesorIA() {

        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(10)
        );

        try {
            By botonOmitir = By.xpath(
                    "//*[normalize-space()='Omitir' and (self::button or self::a)]"
            );

            wait.until(
                    ExpectedConditions.elementToBeClickable(botonOmitir)
            ).click();

            System.out.println("AsesorIA cerrada correctamente");

        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("No apareció AsesorIA");
        }
    }

    public void aceptarCookies() {

        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(10)
        );

        try {
            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id("testId-accept-cookies-btn")
                    )
            ).click();

            System.out.println("Cookies aceptadas correctamente");

        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("No apareció el aviso de cookies");
        }
    }
}

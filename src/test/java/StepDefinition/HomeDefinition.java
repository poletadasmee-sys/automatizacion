
package StepDefinition;

import ObjectPage.HomePage;
import io.cucumber.java.en.And;

public class HomeDefinition {

    private HomePage homePage = new HomePage();

    @And("gestiono el aviso de cookies si está visible")
    public void gestionarCookies() {

        homePage.cerrarAsesorIA();
        homePage.aceptarCookies();
    }
}

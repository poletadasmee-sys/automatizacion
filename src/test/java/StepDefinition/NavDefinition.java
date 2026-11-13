
package StepDefinition;

import Constant.Navegador;
import Control.DriverContext;
import Control.NavSelector;
import io.cucumber.java.en.Given;
import org.junit.Assert;

public class NavDefinition {

    @Given("que ingreso a la página principal de Sodimac Chile")
    public void ingresarSodimacChile() {

        String nav = System.getProperty("nav", "default");
        Navegador navegador = NavSelector.seleccionNavegador(nav);

        String url = "https://www.sodimac.cl/sodimac-cl";

        DriverContext.setUp(navegador, url);

        String urlActual = DriverContext.getDriver().getCurrentUrl();

        Assert.assertEquals(
                "No se ingresó correctamente a Sodimac",
                url,
                urlActual
        );

        System.out.println("Sodimac abierto correctamente");
    }
}

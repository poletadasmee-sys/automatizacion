package StepDefinition;

import Constant.Navegador;
import Control.DriverContext;
import Control.NavSelector;
import ObjectPage.IluminacionPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

/**
 * Steps de iluminacion.feature (@qa04).
 *
 * IMPORTANTE (orden de ejecución): el driver se crea en el step
 * "que ingreso a la página principal de Sodimac Chile" (Antecedentes) y recién ahí se instancia
 * IluminacionPage. Todos los demás steps de esta clase dependen de que ese step se haya ejecutado
 * antes en el mismo escenario (Cucumber crea una instancia nueva de esta clase por escenario).
 */
public class IluminacionDefinition {
    private IluminacionPage iluminacionPage;

    @Given("que ingreso a la página principal de Sodimac Chile")
    public void ingresarALaPaginaPrincipal() {
        String nav = System.getProperty("nav", "default");
        Navegador navegador = NavSelector.seleccionNavegador(nav);
        DriverContext.setUp(navegador, IluminacionPage.HOME_URL);
        iluminacionPage = new IluminacionPage();
        iluminacionPage.ingresarAlInicio();
    }

    @And("gestiono el aviso de cookies si está visible")
    public void gestionarAvisoCookies() {
        iluminacionPage.omitirModalConoceIaSiVisible();
        iluminacionPage.gestionarCookiesSiVisible();
    }

    @And("selecciono la categoría {string} desde el menú principal")
    public void seleccionarCategoria(String categoria) {
        Assert.assertTrue("No se encontró la categoría '" + categoria + "' dentro del menú principal",
                iluminacionPage.menuPrincipal(categoria));
    }

    @Then("debo visualizar el título o contexto {string}")
    public void validarContexto(String contexto) {
        // Nota: la página valida siempre contra "iluminacion"; el parámetro solo se usa en el mensaje.
        Assert.assertTrue("No se visualiza el contexto " + contexto,
                iluminacionPage.esTituloIluminacionVisible());
    }

    @And("debo disponer de enlaces o tarjetas de subcategoría")
    public void validarSubcategorias() {
        Assert.assertTrue("No se encontraron enlaces o tarjetas de subcategoría",
                iluminacionPage.existenSubcategorias());
    }

    @And("las imágenes del contenido principal deben aparecer cargadas sin icono de error")
    public void validarImagenesPrincipales() {
        Assert.assertTrue("Hay imágenes del contenido principal sin cargar",
                iluminacionPage.estanImagenesCargadasSinError());
    }

    @Given("que accedo a un listado de lámparas con el filtro {string} visible")
    public void listadoLamparas(String filtro) {
        pagina().listadoLamparas(filtro);
    }

    @When("marco una opción de {string} que tenga resultados")
    public void marcarOpcionDeFiltro(String filtro) {
        pagina().marcarOpcionDeFiltro(filtro);
    }

    @Then("la opción seleccionada debe mostrarse activa")
    public void validarOpcionActiva() {
        Assert.assertTrue("El filtro no quedó aplicado: la opción no está marcada o su parámetro no está en la URL",
                pagina().opcionActiva());
    }

    @And("el catálogo debe actualizar visualmente sus resultados")
    public void validarCatalogoActualizado() {
        Assert.assertTrue("La cantidad de resultados no disminuyó tras aplicar el filtro",
                pagina().catalogoActualizado());
    }

    @When("desmarco la misma opción")
    public void desmarcarOpcion() {
        pagina().desmarcarOpcionDeFiltro();
    }

    @Then("debe desaparecer su estado seleccionado")
    public void validarOpcionDesmarcada() {
        Assert.assertTrue("El filtro sigue aplicado o no se restauró la cantidad inicial de resultados",
                pagina().opcionNoSeleccionada());
    }

    @Given("que tengo una opción de filtro activa en un listado de iluminación")
    public void prepararFiltroActivo() {
        pagina().prepararListadoConFiltroActivo();
    }

    @When("selecciono un criterio de ordenamiento distinto del actual")
    public void cambiarOrdenamiento() {
        pagina().cambiarOrdenamiento();
    }

    @Then("el criterio elegido debe quedar seleccionado")
    public void validarOrdenamiento() {
        Assert.assertTrue("El criterio de ordenamiento no quedó seleccionado",
                pagina().ordenamientoSeleccionado());
    }

    @And("la opción de filtro debe continuar activa")
    public void validarFiltroConOrdenamiento() {
        Assert.assertTrue("El filtro dejó de estar activo al ordenar",
                pagina().opcionActiva());
    }

    @Given("que selecciono una lámpara disponible desde un listado")
    public void seleccionarLampara() {
        pagina().seleccionarLamparaDesdeListado();
    }

    @Then("debo visualizar el nombre de la lámpara")
    public void validarNombreLampara() {
        Assert.assertTrue("No se visualiza el nombre de la lámpara",
                pagina().nombreProductoVisible());
    }

    @And("debe mostrarse al menos una imagen del producto")
    public void validarImagenProducto() {
        Assert.assertTrue("No se visualiza la imagen del producto",
                pagina().imagenProductoVisible());
    }

    @And("debe existir información de precio o disponibilidad comercial visible")
    public void validarInformacionComercial() {
        Assert.assertTrue("No se encontró precio ni disponibilidad comercial",
                pagina().informacionComercialVisible());
    }

    private IluminacionPage pagina() {
        if (iluminacionPage == null) {
            throw new IllegalStateException(
                    "La página de Iluminación aún no se ha inicializado; primero debe abrirse el navegador.");
        }
        return iluminacionPage;
    }
}

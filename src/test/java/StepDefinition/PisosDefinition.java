package StepDefinition;

import ObjectPage.PisosPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class PisosDefinition {

    private final PisosPage pisosPage = new PisosPage();

    @Given("que ingreso a la página principal de Sodimac Chile")
    public void queIngresoALaPaginaPrincipalDeSodimacChile() {
        Assert.assertTrue("No se pudo abrir la página principal", pisosPage.abrirPaginaPrincipal());
    }

    @And("gestiono el aviso de cookies si está visible")
    public void gestionoElAvisoDeCookiesSiEstaVisible() {
        pisosPage.gestionarCookiesSiExiste();
    }

    @And("selecciono la categoría {string} desde el menú principal")
    public void seleccionoLaCategoriaDesdeElMenuPrincipal(String categoria) {
        Assert.assertTrue("No se pudo seleccionar la categoría " + categoria, pisosPage.seleccionarCategoria(categoria));
    }

    @Then("debo visualizar el contexto de {string}")
    public void deboVisualizarElContextoDe(String contexto) {
        Assert.assertTrue("No se visualiza el contexto de " + contexto, pisosPage.validarContexto(contexto));
    }

    @And("debo encontrar enlaces de revestimientos o tipos de pisos")
    public void deboEncontrarEnlacesDeRevestimientosOTiposDePisos() {
        Assert.assertTrue("No se encontraron enlaces de revestimientos o tipos de pisos", pisosPage.validarEnlacesRevestimientos());
    }

    @When("selecciono un tipo de piso o revestimiento visible")
    public void seleccionoUnTipoDePisoORevestimientoVisible() {
        Assert.assertTrue("No se pudo seleccionar un tipo de piso o revestimiento visible", pisosPage.seleccionarTipoDePisoVisible());
    }

    @Then("debo visualizar un listado relacionado con el tipo seleccionado")
    public void deboVisualizarUnListadoRelacionadoConElTipoSeleccionado() {
        Assert.assertTrue("No se visualiza el listado relacionado", pisosPage.validarListadoRelacionado());
    }

    @And("la ruta de navegación debe identificar el contexto de la sección cuando esté presente")
    public void laRutaDeNavegacionDebeIdentificarElContextoDeLaSeccionCuandoEstePresente() {
        Assert.assertTrue("La ruta de navegación no identifica el contexto", pisosPage.validarRutaNavegacion());
    }

    @Given("que accedo a un listado de pisos con tres o más precios comparables")
    public void queAccedoAUnListadoDePisosConTresOMasPreciosComparables() {
        Assert.assertTrue("No hay suficiente información para comparar precios", pisosPage.accederListadoConPreciosComparables());
    }

    @When("selecciono el criterio {string} en el selector web")
    public void seleccionoElCriterioEnElSelectorWeb(String criterio) {
        Assert.assertTrue("No se pudo seleccionar el criterio de orden " + criterio, pisosPage.seleccionarCriterio(criterio));
    }

    @Then("el selector debe mostrar {string}")
    public void elSelectorDebeMostrar(String criterio) {
        Assert.assertTrue("El selector no muestra el criterio esperado", pisosPage.validarSelectorOrden(criterio));
    }

    @And("los precios comparables deben quedar ordenados de forma {string}")
    public void losPreciosComparablesDebenQuedarOrdenadosDeForma(String direccion) {
        Assert.assertTrue("Los precios no están ordenados según la dirección indicada", pisosPage.validarPrecioOrdenado(direccion));
    }

    @Given("que identifico un producto de Pisos con nombre visible en el listado")
    public void queIdentificoUnProductoDePisosConNombreVisibleEnElListado() {
        Assert.assertTrue("No se identificó un producto con nombre visible", pisosPage.identificarProductoVisible());
    }

    @When("abro la ficha del producto")
    public void abroLaFichaDelProducto() {
        Assert.assertTrue("No se pudo abrir la ficha del producto", pisosPage.abrirFichaProducto());
    }

    @Then("debo ver el mismo nombre en el encabezado de la ficha")
    public void deboVerElMismoNombreEnElEncabezadoDeLaFicha() {
        Assert.assertTrue("No se visualiza el nombre en la ficha del producto", pisosPage.validarNombreFicha());
    }

    @And("debo visualizar una imagen del revestimiento y su precio disponible")
    public void deboVisualizarUnaImagenDelRevestimientoYSuPrecioDisponible() {
        Assert.assertTrue("La ficha no muestra imagen y precio", pisosPage.validarImagenYPrecio());
    }
}
package StepDefinition;

import ObjectPage.ElectroHogarPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
public class ElectrohogarDefinition {

    private ElectroHogarPage electroHogarPage;

//Background
    @Given("que ingreso a la página principal de Sodimac Chile {string}")
    public void ingresoPrincipal(String url) {
        electroHogarPage = ElectroHogarPage.ingresarPaginaPrincipal(url);
    }

    @And("gestiono el aviso de cookies si está visible")
    public void mensajeCookies() {
       electroHogarPage.revisarCookies();
    }

    //Smoke
    @And("selecciono la categoría {string} desde el menú principal")
    public void seleccionarCategoria(String categoria) {
        electroHogarPage.desplegarMenu();
        electroHogarPage.irAElectroDomesticos(categoria);
    }

    @Then("debo visualizar el listado de Electrodomésticos")
    public void validarListadoElectrodomesticos() {
        electroHogarPage.validarListadoElectrodomesticos();
    }

    //filtros
    @Given("que me encuentro en un listado de electrodomésticos con filtro Marca")
    public void validarFiltroMarca() {
        electroHogarPage.buscarMarca();
    }

    @When("selecciono una marca con productos disponibles")
    public void seleccionarMarcaDisponible() {
        electroHogarPage.seleccionarMarcaDisponible();
    }

    @Then("la marca debe aparecer seleccionada")
    public void validarMarcaSeleccionada() {
        electroHogarPage.validarMarcaSeleccionada();
    }

    @And("los productos que indiquen marca en su tarjeta deben coincidir con el filtro")
    public void validarMarcaEnProductos() {
        electroHogarPage.validarProductosDeMarcaSeleccionada();
    }

    @When("retiro el filtro Marca")
    public void retirarFiltroMarca() {
        electroHogarPage.retirarFiltroMarca();
    }

    @Then("esa marca debe quedar desmarcada")
    public void validarMarcaDesmarcada() {
        electroHogarPage.validarMarcaDesmarcada();
    }

    //@ordenamiento
    @Given("que visualizo un listado de electrodomésticos con al menos tres precios comparables")
    public void seleccionarProductos() {
        electroHogarPage.seleccionProducto();
    }

    @When("ordeno los productos de menor a mayor precio")
    public void ordenarProductos() {
        electroHogarPage.validarOrdenamientoAscendente();
    }

    @Then("el criterio de ordenamiento debe quedar seleccionado")
    public void validarCriterioOrdenamiento() {
        electroHogarPage.validarOrdenMenorAMayorSeleccionado();
    }

    @And("los importes visibles comparables deben estar en orden ascendente")
    public void validarPreciosAscendentes() {
        electroHogarPage.confirmarOrden();
    }

    //detalle
    @Given("que abro la ficha de un electrodoméstico desde su tarjeta")
    public void abrirFichaProducto() {
        electroHogarPage.abrirFichaProductoSeleccionado();
    }

    @Then("su nombre debe coincidir con la tarjeta seleccionada")
    public void validarNombreFicha() {
        electroHogarPage.validarNombreFichaCoincide();
    }

    @And("debo visualizar fotografía principal, precio y descripción o especificaciones")
    public void validarAtributosFicha() {
        electroHogarPage.validarAtributosFicha();
    }

    @And("debe estar visible el control comercial principal cuando el producto esté disponible")
    public void validarControlComercial() {
        electroHogarPage.validarControlComercialFicha();
    }

    @And("//prueba que el control comercial principal no esté visible cuando el producto esté agotado")

}
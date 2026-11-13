
package StepDefinition;

import ObjectPage.ConstruccionPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;


public class ConstruccionDefinition {

    private ConstruccionPage construccionPage = new ConstruccionPage();

    @And("selecciono la categoría {string} desde el menú principal")
    public void seleccionarCategoria(String categoria) {

        construccionPage.abrirMenu();
        construccionPage.seleccionarCategoria(categoria);
    }
    @Then("debo visualizar el encabezado correspondiente a {string}")
    public void encabezado(String categoria){
        construccionPage.encabezado(categoria);
    }
    @And("debo visualizar el área de productos o subcategorías de construcción")
    public void validarSubcategorias() {
        construccionPage.validarSubcategorias();
    }
    @And("la navegación principal debe continuar disponible")
    public void validarNavegacion() {
        construccionPage.validarNavegacion();
    }
    @When("selecciono la subcategoría visible {string}")
    public void seleccionarSubcategoria(String subcategoria) {
        construccionPage.seleccionarSubcategoria(subcategoria);
    }
    @Then("debo visualizar el título o ruta de navegación de {string}")
    public void validarTituloSubcategoria(String subcategoria) {
        construccionPage.validarTituloSubcategoria(subcategoria);
    }
    @And("los enlaces de navegación deben permitir regresar a Construcción")
    public void validarRegresoConstruccion() {
        construccionPage.validarRegresoConstruccion();
    }
    @Given("que estoy en un listado de productos de Construcción con filtros disponibles")
    public void filtrosdisponibles() {
        construccionPage.filtrosdisponibles();
    }
@When("activo una opción de filtro que tenga resultados")
    public void filtrosresultados() {
        construccionPage.filtrosresultados();

}
@Then("debo visualizar su selección activa en la interfaz")
    public void seleccionactiva(){
        construccionPage.seleccionactiva();

}
    @And("el listado debe mostrar productos acordes a la opción seleccionada")
    public void validarProductosFiltrados() {
        construccionPage.validarProductosFiltrados();
    }
    @When("retiro ese filtro usando el control web")
    public void retirarFiltro(){
        construccionPage.retirarFiltro();
    }
    @Then("la opción no debe permanecer seleccionada")
    public void validarFiltroRetirado() {
        construccionPage.validarFiltroRetirado();
    }
    @Given("que existe al menos una tarjeta de producto disponible en Construcción")
    public void validarTarjetaDisponible() {
        construccionPage.validarTarjetaDisponible();
    }
    @When("registro el nombre visible del primer producto y abro su detalle")
    public void abrirDetallePrimerProducto() {
        construccionPage.abrirDetallePrimerProducto();
    }
    @Then("la ficha debe mostrar el mismo nombre de producto")
    public void validarNombreFichaProducto() {
        construccionPage.validarNombreFichaProducto();
    }
    @And("debo visualizar imagen principal, información comercial y precio cuando correspondan")
    public void validarInformacionFichaProducto() {
        construccionPage.validarInformacionFichaProducto();
    }
}



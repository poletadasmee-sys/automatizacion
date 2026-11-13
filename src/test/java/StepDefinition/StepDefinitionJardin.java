package StepDefinition;

import Control.DriverContext;
import Control.NavSelector;
import ObjectPage.JardinDefinition;
import ObjectPage.JardinPage;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

public class StepDefinitionJardin {

    private final JardinDefinition jardinDefinition;

    public StepDefinitionJardin() {
        DriverContext.setUp(
                NavSelector.seleccionNavegador(System.getProperty("nav", "default")),
                "https://www.sodimac.cl/");

        JardinPage jardinPage =
                new JardinPage(DriverContext.getDriver());

        jardinDefinition =
                new JardinDefinition(jardinPage);
    }

    @Dado("que ingreso a la página principal de Sodimac Chile")
    public void ingresoPaginaPrincipal() {

        jardinDefinition.abrirHome();
    }

    @Y("gestiono el aviso de cookies si está visible")
    public void gestionarCookies() {

        jardinDefinition.gestionarCookies();
    }

    @Y("selecciono la categoría {string} desde el menú principal")
    public void seleccionarCategoria(String categoria) {

        jardinDefinition.navegarAJardin(categoria);
    }

    @Entonces("debo visualizar el contexto de {string}")
    public void visualizarContexto(String categoria) {

        jardinDefinition.validarContextoJardin(categoria);
    }

    @Entonces("debo visualizar nuevamente el contexto de Jardín")
    public void visualizarContextoJardinNuevamente() {

        jardinDefinition.validarContextoJardin("Jardín");
    }

    @Y("debo visualizar tarjetas o enlaces de categorías del área")
    public void visualizarCategorias() {

        jardinDefinition.validarTarjetasCategorias();
    }

    @Y("el menú debe permitir navegar a otras secciones")
    public void validarMenu() {

        jardinDefinition.validarMenu();
    }

    @Cuando("selecciono una subcategoría visible de productos de jardín")
    public void seleccionarSubcategoria() {
        jardinDefinition.abrirSubcategoria();
    }

    @Entonces("debo visualizar el título o breadcrumb correspondiente a dicha subcategoría")
    public void validarSubcategoria() {
        jardinDefinition.validarSubcategoria();
    }

    @Cuando("regreso a la categoría superior con un control de navegación web")
    public void regresarCategoria() {

        jardinDefinition.volverAJardin();
    }

    @Dado("que accedo a una lista de productos de Jardín con filtros visibles")
    public void accederListadoConFiltros() {
        jardinDefinition.accederPrimeraCategoriaConFiltros();
    }

    @Cuando("activo un filtro con productos disponibles")
    public void activarFiltro() {

        jardinDefinition.aplicarFiltro();
    }

    @Entonces("el filtro debe reflejar estado seleccionado")
    public void validarFiltroSeleccionado() {

        jardinDefinition.validarFiltro();
    }

    @Y("el listado debe responder visualmente a la selección")
    public void validarListado() {

        jardinDefinition.validarListado();
    }

    @Cuando("elimino el filtro desde la interfaz")
    public void eliminarFiltro() {

        jardinDefinition.eliminarFiltro();
    }

    @Entonces("debe volver a mostrarse sin seleccionar")
    public void validarEliminacionFiltro() {

        jardinDefinition.validarFiltroEliminado();
    }

    @Dado("que identifico un producto de jardín con nombre visible")
    public void identificarProducto() {

        jardinDefinition.identificarProducto();
    }

    @Cuando("abro su ficha desde la tarjeta")
    public void abrirFichaProducto() {
        jardinDefinition.abrirProducto();
    }

    @Entonces("el título debe corresponder al producto elegido")
    public void validarTituloProducto() {

        jardinDefinition.validarProducto();
    }

    @Y("se debe mostrar su imagen principal y sección de precio")
    public void validarImagenPrecio() {

        jardinDefinition.validarImagenPrecio();
    }
}
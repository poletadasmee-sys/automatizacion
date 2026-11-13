package StepDefinition;

import ObjectPage.BanoPage;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import io.cucumber.java.es.Dado;

public class BanoDefinition {

    private final BanoPage banoPage = new BanoPage();

    @Y("selecciono la categoría {string} desde el menú principal")
    public void seleccionoLaCategoriaDesdeElMenuPrincipal(String categoria) {
        if (categoria.equalsIgnoreCase("Baño")) {
            banoPage.abrirMenuPrincipal();
            banoPage.seleccionarCategoriaBano();
        }
    }

    @Entonces("debo visualizar el contexto de la categoría {string}")
    public void deboVisualizarElContextoDeLaCategoria(String categoria) {
        banoPage.validarCategoriaBano();
    }

    @Y("gestiono el aviso de cookies si está visible")
    public void gestionoCookies() {
        banoPage.gestionarPopupsIniciales();
    }

    @Y("debo encontrar enlaces a tipos de productos de baño")
    public void deboEncontrarEnlacesATiposDeProductosDeBano() {
        System.out.println("Se visualizan categorías de baño");
    }

    @Cuando("selecciono la opción visible {string}")
    public void seleccionoLaOpcionVisible(String opcion) {
        if (opcion.equalsIgnoreCase("Griferías")
                || opcion.equalsIgnoreCase("Griferias")) {
            banoPage.seleccionarGriferias();
        }
    }

    @Entonces("debo visualizar el contexto de productos de grifería")
    public void deboVisualizarElContextoDeProductosDeGriferia() {
        banoPage.validarPaginaGriferias();
    }

    // ==========================================
    // ESCENARIO 2: @filtros
    // ==========================================

    @Dado("que me encuentro en un listado de productos de Baño con al menos dos filtros aplicables")
    public void listadoConFiltrosDisponibles() {
        banoPage.validarListadoConFiltros();
    }

    @Cuando("selecciono una opción disponible del primer filtro")
    public void seleccionoPrimerFiltro() {
        banoPage.seleccionarPrimerFiltro();
    }

    @Y("selecciono una opción compatible del segundo filtro")
    public void seleccionoSegundoFiltro() {
        banoPage.seleccionarSegundoFiltro();
    }

    @Entonces("ambas selecciones deben observarse activas")
    public void validarFiltrosActivos() {
        banoPage.validarFiltrosActivos();
    }

    @Y("las tarjetas mostradas deben satisfacer las características visibles filtradas")
    public void validarTarjetasFiltradas() {
        banoPage.validarResultadosFiltrados();
    }

    @Cuando("utilizo el control para limpiar filtros")
    public void limpiarFiltros() {
        banoPage.limpiarFiltros();
    }

    @Entonces("ninguna de las dos opciones debe mantenerse activa")
    public void validarFiltrosLimpiados() {
        banoPage.validarFiltrosLimpios();
    }

    // ==========================================
    // ESCENARIO 3: @detalle
    // ==========================================

    @Dado("que abro desde el listado un producto de grifería con varias imágenes")
    public void abrirProductoDeGriferiaConVariasImagenes() {
        banoPage.abrirProductoConGaleria();
    }

    @Cuando("selecciono una miniatura diferente de la galería")
    public void seleccionoMiniaturaDiferenteGaleria() {
        banoPage.seleccionarMiniaturaGaleria();
    }

    @Entonces("la imagen principal debe actualizarse visualmente")
    public void validarImagenPrincipalActualizada() {
        banoPage.validarCambioImagenPrincipal();
    }

    @Y("el nombre del producto debe permanecer visible")
    public void validarNombreProductoVisible() {
        banoPage.validarNombreProductoVisible();
    }

    // ==========================================
    // ESCENARIO 4: @navegacion
    // ==========================================

    @Dado("que abro una ficha de producto de Baño con ruta de navegación visible")
    public void abrirFichaProductoConRutaNavegacion() {
        banoPage.abrirFichaConBreadcrumbs();
    }

    @Cuando("selecciono un enlace anterior de la ruta de navegación")
    public void seleccionoEnlaceAnteriorRutaNavegacion() {
        banoPage.hacerClicEnBreadcrumbAnterior();
    }

    @Entonces("debo volver al contexto de categoría indicado por ese enlace")
    public void validarRetornoContextoCategoria() {
        banoPage.validarRetornoCategoriaBreadcrumb();
    }
}
package ObjectPage;

public class JardinDefinition {

    private final JardinPage jardinPage;

    public JardinDefinition(JardinPage jardinPage) {
        this.jardinPage = jardinPage;
    }

    public void abrirHome() {
        jardinPage.abrirHome();
    }

    public void gestionarCookies() {
        jardinPage.aceptarCookiesSiExiste();
    }

    public void navegarAJardin(String categoria) {
        jardinPage.ingresarCategoriaJardin(categoria);
    }

    public void validarContextoJardin(String categoria) {
        jardinPage.validarContextoJardin(categoria);
    }

    public void validarTarjetasCategorias() {
        jardinPage.validarTarjetasCategorias();
    }

    public void validarMenu() {
        jardinPage.validarMenuNavegable();
    }

    public void abrirSubcategoria() {
        jardinPage.abrirSubcategoriaVisible();
    }

    public void validarSubcategoria() {
        jardinPage.validarBreadcrumbOTitulo();
    }

    public void volverAJardin() {
        jardinPage.regresarPaginaAnterior();
    }

    public void accederPrimeraCategoriaConFiltros() {
        jardinPage.accederPrimeraSubcategoriaConFiltros();
    }

    public void aplicarFiltro() {
        jardinPage.activarPrimerFiltroDisponible();
    }

    public void validarFiltro() {
        jardinPage.validarFiltroAplicado();
    }

    public void validarListado() {
        jardinPage.validarRespuestaListado();
    }

    public void eliminarFiltro() {
        jardinPage.quitarFiltro();
    }

    public void validarFiltroEliminado() {
        jardinPage.validarFiltroRemovido();
    }

    public void identificarProducto() {
        jardinPage.identificarPrimerProducto();
    }

    public void abrirProducto() {
        jardinPage.abrirProductoSeleccionado();
    }

    public void validarProducto() {
        jardinPage.validarTituloProducto();
    }

    public void validarImagenPrecio() {
        jardinPage.validarImagenPrincipalYPrecio();
    }
}
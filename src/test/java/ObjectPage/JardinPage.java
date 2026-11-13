package ObjectPage;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.text.Normalizer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class JardinPage {

    private record Subcategoria(String nombre, String url) {}

    private final WebDriver driver;
    private String urlProductoSeleccionado;
    private String urlCategoriaJardin;
    private boolean tituloProductoValido;
    private final List<String> diferenciasSubcategorias = new ArrayList<>();
    private int subcategoriasRevisadas;

    public JardinPage(WebDriver driver) {
        this.driver = driver;
    }

    // ==========================
    // Localizadores
    // ==========================

    private final By tituloPagina =
            By.tagName("h1");

    private final By linksPagina =
            By.tagName("a");

    private final By productos =
            By.cssSelector("[data-testid*='product'], .pod-item, .search-results-product");

    private static final String FORMATO_RESULTADOS_SIN_FILTRO =
            "^1\\s*-\\s*48\\s+de\\s+\\d+\\s+Resultados$";
    private static final String RESULTADOS_ENVIO_GRATIS = "1 - 15 de 15 Resultados";
    private static final By CONTADOR_RESULTADOS = By.id("search_numResults");
    private static final By FILTRO_ENVIO_GRATIS = By.id("Envío-gratis");
    private static final By ETIQUETA_FILTRO_ENVIO_GRATIS =
            By.cssSelector("label[for='Envío-gratis']");
    private static final By TARJETAS_PRODUCTOS =
            By.cssSelector("#testId-searchResults-products [data-testid='ssr-pod']");
    private static final Set<String> PALABRAS_COMUNES =
            Set.of("a", "al", "de", "del", "el", "en", "e", "la", "las",
                    "los", "para", "por", "un", "una", "y");

    // ==========================
    // NAVEGACIÓN
    // ==========================

    public void abrirHome() {
        driver.get("https://www.sodimac.cl/");
    }

    public void aceptarCookiesSiExiste() {
        List<WebElement> botonesCookies =
                driver.findElements(By.id("onetrust-accept-btn-handler"));

        if (!botonesCookies.isEmpty() && botonesCookies.get(0).isDisplayed()) {
            botonesCookies.get(0).click();
        }
    }

    public void ingresarCategoriaJardin(String categoria) {
        String slugCategoria = normalizarCategoria(categoria);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement enlaceJardin = Objects.requireNonNull(wait.until(webDriver ->
                webDriver.findElements(By.cssSelector("a[href]"))
                        .stream()
                        .filter(enlace -> {
                            String href = enlace.getDomAttribute("href");
                            return href != null
                                    && href.toLowerCase(Locale.ROOT).contains("/lista/")
                                    && href.toLowerCase(Locale.ROOT).contains("/" + slugCategoria);
                        })
                        .findFirst()
                        .orElse(null)), "No se encontró el enlace de la categoría Jardín");
        String urlCategoria = Objects.requireNonNull(
                enlaceJardin.getDomProperty("href"),
                "El enlace de la categoría Jardín no tiene una URL");
        urlCategoriaJardin = urlCategoria;
        driver.get(urlCategoria);
    }

    // ==========================
    // VALIDACIONES
    // ==========================

    public void validarContextoJardin(String categoria) {
        String slugCategoria = normalizarCategoria(categoria);
        Assert.assertTrue(
                "La URL actual no corresponde a la categoría " + categoria,
                Objects.requireNonNull(driver.getCurrentUrl(), "La URL actual no está disponible")
                        .toLowerCase(Locale.ROOT).contains(slugCategoria));
    }

    private String normalizarCategoria(String categoria) {
        return Normalizer.normalize(categoria, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9]", "")
                .toLowerCase(Locale.ROOT);
    }

    public void validarTarjetasCategorias() {

        List<WebElement> elementos =
                driver.findElements(linksPagina);

        Assert.assertFalse(elementos.isEmpty());
    }

    public void validarMenuNavegable() {

        Assert.assertTrue(
                driver.findElement(By.tagName("body")).isDisplayed());
    }

    // ==========================
    // SUBCATEGORÍA
    // ==========================

    public void abrirSubcategoriaVisible() {
        String urlCategoria = Objects.requireNonNull(
                driver.getCurrentUrl(), "La URL actual de la categoría no está disponible");
        urlCategoriaJardin = urlCategoria;
        diferenciasSubcategorias.clear();
        subcategoriasRevisadas = 0;

        Map<String, Subcategoria> subcategorias = new LinkedHashMap<>();
        By seccionCategoriasJardin = By.xpath(
                "//div[@data-testid='collapsed-cards']"
                        + "[.//a[@data-testid='card-link'][contains(@href, '/lista/CATG')]]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement seccion = Objects.requireNonNull(wait.until(webDriver ->
                webDriver.findElements(seccionCategoriasJardin)
                        .stream()
                        .findFirst()
                        .orElse(null)), "No se encontró la sección de categorías de Jardín");

        for (WebElement enlace : seccion.findElements(
                By.cssSelector("a[data-testid='card-link'][href]"))) {
            String nombre = enlace.getText().trim();
            String url = enlace.getDomProperty("href");
            if (nombre.isEmpty()) {
                String alt = enlace.findElement(By.cssSelector("img[alt]"))
                        .getDomAttribute("alt");
                nombre = alt == null ? "" : alt.trim();
            }
            if (!nombre.isEmpty() && url != null
                    && !ruta(url).equals(ruta(urlCategoria))) {
                subcategorias.putIfAbsent(
                        url + "|" + normalizarCategoria(nombre),
                        new Subcategoria(nombre, url));
            }
        }

        List<Subcategoria> destinos = new ArrayList<>(subcategorias.values());

        Assert.assertFalse("No se encontraron subcategorías de Jardín", destinos.isEmpty());

        System.out.printf("Subcategorías visibles para Jardín: %d%n", destinos.size());
        for (int i = 0; i < destinos.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, destinos.get(i).nombre());
        }

        for (Subcategoria destino : destinos) {
            String nombreEsperado = destino.nombre();
            String tituloMostrado;

            try {
                driver.get(destino.url());
                WebElement titulo = Objects.requireNonNull(wait.until(webDriver ->
                        webDriver.findElements(tituloPagina).stream()
                                .filter(WebElement::isDisplayed)
                                .findFirst()
                                .orElse(null)), "No se encontró un título visible");
                tituloMostrado = titulo.getText().trim();
            } catch (TimeoutException error) {
                tituloMostrado = "No se encontró un título visible";
            } finally {
                driver.get(urlCategoria);
            }

            subcategoriasRevisadas++;
            String nombreEsperadoNormalizado = normalizarCategoria(nombreEsperado);
            String tituloMostradoNormalizado = normalizarCategoria(tituloMostrado);
            boolean coincide = !tituloMostradoNormalizado.isEmpty()
                    && (nombreEsperadoNormalizado.equals(tituloMostradoNormalizado)
                    || nombreEsperadoNormalizado.contains(tituloMostradoNormalizado)
                    || tituloMostradoNormalizado.contains(nombreEsperadoNormalizado)
                    || coincidePorPalabras(nombreEsperado, tituloMostrado));
            String resultado = coincide ? "EXITOSO" : "NO EXITOSO";
            String mensaje = coincide
                    ? "Se esperaba la subcategoría '" + nombreEsperado
                    + "', y el título mostrado es '" + tituloMostrado + "'"
                    : "Se esperaba la subcategoría '" + nombreEsperado
                    + "', pero el título mostrado es '" + tituloMostrado + "'";

            System.out.printf("%s | %s%n", mensaje, resultado);
            if (!coincide) {
                diferenciasSubcategorias.add(mensaje);
            }
        }
    }

    public void validarBreadcrumbOTitulo() {
        Assert.assertTrue(
                "Se deben revisar todas las subcategorías antes de validarlas",
                subcategoriasRevisadas > 0);
        Assert.assertTrue(
                "Se encontraron diferencias de título:\n"
                        + String.join("\n", diferenciasSubcategorias),
                diferenciasSubcategorias.isEmpty());
    }

    public void regresarPaginaAnterior() {
        if (urlCategoriaJardin != null) {
            driver.get(urlCategoriaJardin);
        } else {
            driver.navigate().back();
        }
    }

    private String ruta(String url) {
        return URI.create(url).normalize().getPath();
    }

    // ==========================
    // FILTROS
    // ==========================

    public void accederPrimeraSubcategoriaConFiltros() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        By enlacesSubcategorias = By.cssSelector(
                "div[data-testid='collapsed-cards'] a[data-testid='card-link'][href]");
        WebElement primeraSubcategoria = Objects.requireNonNull(wait.until(webDriver ->
                webDriver.findElements(enlacesSubcategorias).stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null)), "No se encontró una subcategoría visible");

        String urlSubcategoria = Objects.requireNonNull(
                primeraSubcategoria.getDomProperty("href"),
                "La primera subcategoría no tiene una URL");
        System.out.println("Ingresando a la primera subcategoría de Jardín: "
                + primeraSubcategoria.getText().trim());
        driver.get(urlSubcategoria);

        wait.until(webDriver ->
                !webDriver.findElements(CONTADOR_RESULTADOS).isEmpty()
                        && !webDriver.findElements(ETIQUETA_FILTRO_ENVIO_GRATIS).isEmpty());
        validarContadorSinFiltro("El contador inicial");
        imprimirResumenResultados("Listado inicial sin filtro");
    }

    public void activarPrimerFiltroDisponible() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        WebElement filtro = Objects.requireNonNull(wait.until(webDriver -> {
            List<WebElement> elementos = webDriver.findElements(FILTRO_ENVIO_GRATIS);
            return elementos.isEmpty() ? null : elementos.get(0);
        }), "No se encontró el filtro Envío a domicilio Gratis");
        Assert.assertFalse("El filtro Envío a domicilio Gratis ya estaba seleccionado",
                filtro.isSelected());

        WebElement etiqueta = driver.findElement(ETIQUETA_FILTRO_ENVIO_GRATIS);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", etiqueta);
        wait.until(webDriver ->
                webDriver.findElement(FILTRO_ENVIO_GRATIS).isSelected());
        wait.until(webDriver ->
                RESULTADOS_ENVIO_GRATIS.equals(
                        webDriver.findElement(CONTADOR_RESULTADOS).getText().trim()));

        Assert.assertEquals(
                "El filtro Envío a domicilio Gratis no produjo los 15 resultados esperados",
                RESULTADOS_ENVIO_GRATIS,
                driver.findElement(CONTADOR_RESULTADOS).getText().trim());
        System.out.println("Filtro seleccionado: Envío a domicilio - Gratis");
        imprimirListado();
    }

    public void validarFiltroAplicado() {
        Assert.assertTrue(
                "El filtro Envío a domicilio Gratis debe estar seleccionado",
                driver.findElement(FILTRO_ENVIO_GRATIS).isSelected());
        Assert.assertEquals(
                RESULTADOS_ENVIO_GRATIS,
                driver.findElement(CONTADOR_RESULTADOS).getText().trim());
        System.out.println("Estado del filtro Envío a domicilio - Gratis: seleccionado");
    }

    public void validarRespuestaListado() {
        Assert.assertEquals(
                "Debe mostrarse el listado filtrado de 15 productos",
                15,
                driver.findElements(TARJETAS_PRODUCTOS).size());
    }

    public void quitarFiltro() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        WebElement filtro = driver.findElement(FILTRO_ENVIO_GRATIS);
        Assert.assertTrue("El filtro Envío a domicilio Gratis no está seleccionado",
                filtro.isSelected());

        WebElement etiqueta = driver.findElement(ETIQUETA_FILTRO_ENVIO_GRATIS);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", etiqueta);
        wait.until(webDriver ->
                !webDriver.findElement(FILTRO_ENVIO_GRATIS).isSelected());
        wait.until(webDriver ->
                webDriver.findElement(CONTADOR_RESULTADOS).getText().trim()
                        .matches(FORMATO_RESULTADOS_SIN_FILTRO));
        System.out.println("Filtro eliminado: Envío a domicilio - Gratis");
        validarContadorSinFiltro("El contador después de quitar el filtro");
        imprimirResumenResultados("Listado restaurado sin filtro");
    }

    public void validarFiltroRemovido() {
        Assert.assertFalse(
                "El filtro Envío a domicilio Gratis debe quedar desmarcado",
                driver.findElement(FILTRO_ENVIO_GRATIS).isSelected());
        validarContadorSinFiltro("El contador del listado sin filtro");
        System.out.println("Estado del filtro Envío a domicilio - Gratis: desmarcado");
    }

    private void validarContadorSinFiltro(String descripcion) {
        String contador = driver.findElement(CONTADOR_RESULTADOS).getText().trim();
        Assert.assertTrue(
                descripcion + " no tiene el formato esperado '1 - 48 de N Resultados': "
                        + contador,
                contador.matches(FORMATO_RESULTADOS_SIN_FILTRO));
    }

    private boolean coincidePorPalabras(String nombreEsperado, String tituloMostrado) {
        Set<String> palabrasEsperadas = palabrasSignificativas(nombreEsperado);
        Set<String> palabrasMostradas = palabrasSignificativas(tituloMostrado);
        return !palabrasEsperadas.isEmpty()
                && !palabrasMostradas.isEmpty()
                && (palabrasEsperadas.containsAll(palabrasMostradas)
                || palabrasMostradas.containsAll(palabrasEsperadas));
    }

    private Set<String> palabrasSignificativas(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .trim();
        if (normalizado.isEmpty()) {
            return Set.of();
        }
        return java.util.Arrays.stream(normalizado.split("\\s+"))
                .filter(palabra -> !PALABRAS_COMUNES.contains(palabra))
                .collect(java.util.stream.Collectors.toSet());
    }

    private void imprimirListado() {
        String resultados = driver.findElement(CONTADOR_RESULTADOS).getText().trim();
        List<WebElement> tarjetas = driver.findElements(TARJETAS_PRODUCTOS);
        Assert.assertFalse("No se encontraron productos para mostrar", tarjetas.isEmpty());

        System.out.printf("Listado con filtro Envío a domicilio Gratis: %s%n", resultados);
        System.out.printf("Productos listados en la página: %d%n", tarjetas.size());
        for (int i = 0; i < tarjetas.size(); i++) {
            WebElement tarjeta = tarjetas.get(i);
            List<WebElement> nombres = tarjeta.findElements(By.cssSelector(".pod-subTitle"));
            String nombreProducto = nombres.isEmpty()
                    ? "" : nombres.get(0).getText().trim();
            List<WebElement> marcas = tarjeta.findElements(By.cssSelector(".pod-title"));
            String marca = marcas.isEmpty() ? "" : marcas.get(0).getText().trim();
            if (nombreProducto.isEmpty()) {
                List<WebElement> imagenes = tarjeta.findElements(By.cssSelector("img[alt]"));
                nombreProducto = imagenes.isEmpty()
                        ? "Producto sin nombre visible"
                        : imagenes.get(0).getDomAttribute("alt");
            }
            String nombreCompleto = marca.isEmpty()
                    ? nombreProducto : marca + " - " + nombreProducto;
            System.out.printf("%d. %s%n", i + 1, nombreCompleto);
        }
    }

    private void imprimirResumenResultados(String descripcion) {
        System.out.printf("%s: %s%n",
                descripcion,
                driver.findElement(CONTADOR_RESULTADOS).getText().trim());
    }

    // ==========================
    // PRODUCTO
    // ==========================

    public void identificarPrimerProducto() {

        List<WebElement> lista =
                driver.findElements(productos);

        Assert.assertFalse(lista.isEmpty());

        WebElement tarjeta = lista.get(0);
        List<WebElement> enlaces = tarjeta.findElements(By.cssSelector("a[href]"));
        String href = enlaces.isEmpty()
                ? tarjeta.getDomAttribute("href")
                : enlaces.get(0).getDomAttribute("href");

        Assert.assertNotNull("La tarjeta no contiene un enlace de producto", href);
        urlProductoSeleccionado = href;
    }

    public void abrirProductoSeleccionado() {
        Assert.assertNotNull("Primero se debe identificar un producto", urlProductoSeleccionado);
        tituloProductoValido = false;
        driver.get(urlProductoSeleccionado);
    }

    public void validarTituloProducto() {
        List<WebElement> titulos = driver.findElements(By.tagName("h1"));
        WebElement tituloVisible = titulos.stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null);
        tituloProductoValido = tituloVisible != null;
        String tituloProducto = tituloVisible == null
                ? "No se encontró un título visible"
                : tituloVisible.getText().trim();
        System.out.printf("Título del producto revisado: %s%n", tituloProducto);
        System.out.printf("Revisión del título: %s%n",
                tituloProductoValido ? "EXITOSO" : "FALLIDO");
    }

    public void validarImagenPrincipalYPrecio() {
        boolean imagenVisible = driver.findElements(By.tagName("img")).stream()
                .anyMatch(WebElement::isDisplayed);
        boolean precioVisible = Objects.requireNonNull(
                driver.getPageSource(), "El contenido de la página no está disponible").contains("$");

        System.out.printf("Revisión de imagen: %s%n",
                imagenVisible ? "EXITOSO" : "FALLIDO");
        System.out.printf("Revisión de precio: %s%n",
                precioVisible ? "EXITOSO" : "FALLIDO");

        Assert.assertTrue(
                "No se encontró un título visible para el producto",
                tituloProductoValido);
        Assert.assertTrue("No se encontró una imagen visible del producto", imagenVisible);
        Assert.assertTrue("No se encontró el indicador de precio ($)", precioVisible);
    }
}
package ObjectPage;

import Control.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Página de la categoría Iluminación de Sodimac Chile (feature: iluminacion.feature, @qa04).
 *
 * Referencia del estilo correcto del proyecto: extiende BasePage, locators private static final By,
 * esperas de BasePage, sin asserts (devuelve boolean/valores) y sin Thread.sleep ni System.out.
 *
 * El driver lo crea el step "que ingreso a la página principal de Sodimac Chile" (IluminacionDefinition)
 * ANTES de instanciar esta página; BasePage lanza IllegalStateException si aún no existe.
 */
public class IluminacionPage extends BasePage {
    public static final String HOME_URL = "https://www.sodimac.cl/sodimac-cl";

    private static final By BODY = By.tagName("body");
    private static final By ACEPTAR_COOKIES = By.id("testId-accept-cookies-btn");
    private static final By BOTON_MENU = By.cssSelector("[data-testid='CategoryButton']");
    private static final By MENU_PRINCIPAL =
            By.cssSelector("[data-testid='SiSNavigationMenuId']");
    private static final By OPCIONES_MENU = By.cssSelector(
            "[data-testid='SiSNavigationMenuId'] a");
    private static final By TITULOS = By.cssSelector("h1, h2, h3, [role='heading']");

    // Enlaces o tarjetas de subcategoría
    private static final By SUBCATEGORIAS = By.cssSelector(
            "a[href*='/lista/CATG10081/'], a[href*='/lista/cat14250003/'], "
                    + "a[href*='/lista/cat40064/']");

    // Imágenes del contenido principal
    private static final By IMAGENES_CONTENIDO = By.cssSelector("img[alt]");

    private static final By LINK_CATEGORIA_LAMPARAS = By.cssSelector(
            "a[href*='/lista/cat40064/Lamparas']");

    private static final By LISTADO_LAMPARAS = By.id("testId-searchResults-products");
    private static final By PRODUCTOS = By.cssSelector(
            "#testId-searchResults-products a[id^='testId-pod-']");
    private static final By TOTAL_RESULTADOS =
            By.id("testId-ProductLandingContainer-totalResults");
    private static final By BOTON_ORDENAMIENTO = By.id("testId-Dropdown-desktop-button");
    private static final By TITULO_PRODUCTO = By.cssSelector("h1");
    private static final By IMAGEN_PRINCIPAL_PRODUCTO = By.id("testId-pod-image-1");

    private static final By BTN_OMITIR_CONOCE_IA = By.xpath(
            "//button[normalize-space(.)='Omitir']");

    private String filtroActual;
    private String opcionFiltroActual;
    private int resultadosAntesDelFiltro;
    private int resultadosConFiltro;
    private String criterioOrdenSeleccionado;

    // ------------------------------------------------------------------ inicio

    public void ingresarAlInicio() {
        esperarPresente(BODY);
    }

    /** El aviso de cookies es opcional: si no aparece en 3 s, se continúa. */
    public void gestionarCookiesSiVisible() {
        WebElement botonAceptar = null;
        cerrarModalIaSiAparece();
        try {
            botonAceptar = esperarClickable(ACEPTAR_COOKIES, 3);
        } catch (TimeoutException e) {
            cerrarModalIaSiAparece();
            try {
                botonAceptar = esperarClickable(ACEPTAR_COOKIES, 3);
            } catch (TimeoutException avisoOpcionalNoVisible) {
                // El aviso es opcional y puede no mostrarse.
            }
        }
        if (botonAceptar != null) {
            cerrarModalIaSiAparece();
            clickPorJs(botonAceptar);
        }
    }

    /** Cierra el aviso inicial del asistente de IA si aparece. */
    public void omitirModalConoceIaSiVisible() {
        try {
            WebElement botonOmitir = esperarClickable(BTN_OMITIR_CONOCE_IA, 15);
            clickPorJs(botonOmitir);
        } catch (TimeoutException e) {
            // El modal es opcional; se revisará de nuevo antes de las siguientes acciones.
        }
    }

    private void cerrarModalIaSiAparece() {
        for (WebElement boton : driver.findElements(BTN_OMITIR_CONOCE_IA)) {
            if (boton.isDisplayed() && boton.isEnabled()) {
                try {
                    boton.click();
                } catch (ElementClickInterceptedException e) {
                    clickPorJs(boton);
                }
                return;
            }
        }
    }

    // ------------------------------------------------------------------ menú

    /**
     * Abre el menú principal y selecciona la categoría indicada.
     *
     * @return false si la categoría no aparece en el menú dentro del timeout; true si se seleccionó
     *         y cargó su contexto (URL o título).
     */
    public boolean menuPrincipal(String nombreCategoria) {
        String categoriaBuscada = normalizarTexto(nombreCategoria);
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            wait.until(d -> ((Number) ((JavascriptExecutor) d)
                    .executeScript("return window.pageYOffset;")).doubleValue() <= 1);
            wait.until(d -> clickBotonMenuVisible(d));
            wait.until(d -> d.findElements(MENU_PRINCIPAL).stream().anyMatch(WebElement::isDisplayed));
            wait.until(d -> clickCategoriaVisible(d, categoriaBuscada, false));
            wait.until(d -> clickCategoriaVisible(d, categoriaBuscada, true));
        } catch (TimeoutException e) {
            return false;
        }

        wait.until(d -> d.getCurrentUrl().toLowerCase(Locale.ROOT).contains("ilumin")
                || algunTituloVisibleContiene(d, categoriaBuscada));
        return true;
    }

    private boolean clickBotonMenuVisible(WebDriver d) {
        for (WebElement boton : d.findElements(BOTON_MENU)) {
            try {
                if (boton.isDisplayed() && boton.isEnabled()) {
                    cerrarModalIaSiAparece();
                    boton.click();
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // El header puede re-renderizarse mientras se inspeccionan sus controles.
            } catch (ElementClickInterceptedException e) {
                cerrarModalIaSiAparece();
                clickPorJs(boton);
                return true;
            }
        }
        return false;
    }

    private boolean clickCategoriaVisible(WebDriver d, String categoriaBuscada, boolean enlaceFinal) {
        for (WebElement opcion : d.findElements(OPCIONES_MENU)) {
            try {
                if (!opcion.isDisplayed() || !opcion.isEnabled()) {
                    continue;
                }
                String href = opcion.getDomAttribute("href");
                boolean esEnlaceFinal = href != null && href.contains("/lista/");
                String textoOpcion = normalizarTexto(opcion.getText());
                boolean correspondeCategoria = textoOpcion.equals(categoriaBuscada);
                boolean correspondeEnlaceVerTodo = textoOpcion.equals("todo " + categoriaBuscada);
                if (enlaceFinal != esEnlaceFinal
                        || !(correspondeCategoria || (enlaceFinal && correspondeEnlaceVerTodo))) {
                    continue;
                }
                cerrarModalIaSiAparece();
                if (!enlaceFinal && "#".equals(href)) {
                    scrollHasta(opcion);
                    new Actions(d).moveToElement(opcion).perform();
                    return true;
                }
                if (enlaceFinal) {
                    scrollHasta(opcion);
                    try {
                        opcion.click();
                    } catch (ElementNotInteractableException e) {
                        clickPorJs(opcion);
                    }
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // El menú puede re-renderizarse al seleccionar una categoría.
            }
        }
        return false;
    }

    private boolean algunTituloVisibleContiene(WebDriver d, String textoNormalizado) {
        return d.findElements(TITULOS).stream()
                .anyMatch(titulo -> {
                    try {
                        return titulo.isDisplayed()
                                && normalizarTexto(titulo.getText()).contains(textoNormalizado);
                    } catch (StaleElementReferenceException ignored) {
                        return false;
                    }
                });
    }

    // ------------------------------------------------------------------ consultas (@smoke)

    /** true si algún título visible contiene "Iluminación". */
    public boolean esTituloIluminacionVisible() {
        try {
            return wait.until(d -> d.findElements(TITULOS).stream()
                    .anyMatch(titulo -> titulo.isDisplayed()
                            && normalizarTexto(titulo.getText()).contains("iluminacion")));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** true si hay al menos un enlace o tarjeta visible de subcategoría. */
    public boolean existenSubcategorias() {
        try {
            return wait.until(d -> d.findElements(SUBCATEGORIAS).stream()
                    .anyMatch(WebElement::isDisplayed));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * true si todas las imágenes del contenido principal cargaron (complete y naturalWidth > 0,
     * es decir, sin icono de error).
     */
    public boolean estanImagenesCargadasSinError() {
        try {
            return wait.until(d -> {
                List<WebElement> imagenes = d.findElements(IMAGENES_CONTENIDO);
                if (imagenes.isEmpty()) {
                    return false;
                }
                return Boolean.TRUE.equals(((JavascriptExecutor) d).executeScript(
                        "const header = document.querySelector('header,[role=\"banner\"]');"
                                + "const headerBottom = header ? header.getBoundingClientRect().bottom : 0;"
                                + "const visible = Array.from(arguments[0]).filter(image => {"
                                + "const rect = image.getBoundingClientRect();"
                                + "return rect.width > 0 && rect.height > 0 && rect.bottom > 0"
                                + " && rect.right > 0 && rect.top < window.innerHeight"
                                + " && rect.left < window.innerWidth && rect.top >= headerBottom;"
                                + "});"
                                + "return visible.length > 0 && visible.every(image =>"
                                + " image.complete && image.naturalWidth > 0);",
                        imagenes));
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ------------------------------------------------------------------ listado / detalle

    /** Abre la categoría Lámparas desde la página de Iluminación y expande el filtro indicado. */
    public void listadoLamparas(String filtro) {
        abrirListadoLamparas();
        filtroActual = normalizarFiltro(filtro);
        abrirFiltro(filtroActual);
    }

    public void marcarOpcionDeFiltro(String filtro) {
        String filtroSolicitado = normalizarFiltro(filtro);
        if (!filtroSolicitado.equals(filtroActual)) {
            throw new IllegalStateException(
                    "El filtro solicitado no coincide con el filtro visible: " + filtro);
        }

        WebElement opcion = primeraOpcionDisponible(filtroActual);
        opcionFiltroActual = textoOpcion(opcion);
        resultadosAntesDelFiltro = cantidadResultados();
        clickEtiquetaOpcion(opcion);
        esperarOpcionSeleccionada(true);
        resultadosConFiltro = wait.until(d -> {
            int resultados = leerCantidadResultados(d);
            return resultados > 0 && resultados < resultadosAntesDelFiltro
                    ? resultados : null;
        });
    }

    public boolean opcionActiva() {
        return opcionFiltroActual != null
                && opcionSeleccionada()
                && urlContieneFiltroActual();
    }

    public boolean catalogoActualizado() {
        if (!opcionActiva() || !existe(LISTADO_LAMPARAS)) {
            return false;
        }
        int resultadosActuales = cantidadResultados();
        return resultadosAntesDelFiltro > 0
                && resultadosActuales > 0
                && resultadosActuales < resultadosAntesDelFiltro;
    }

    public void desmarcarOpcionDeFiltro() {
        if (!opcionActiva()) {
            throw new IllegalStateException("No hay una opción de filtro activa para desmarcar.");
        }
        clickEtiquetaOpcion(opcionPorTexto(filtroActual, opcionFiltroActual));
        esperarOpcionSeleccionada(false);
    }

    public boolean opcionNoSeleccionada() {
        if (opcionFiltroActual == null || opcionSeleccionada()
                || urlContieneFiltroActual()) {
            return false;
        }
        return resultadosConFiltro > 0
                && wait.until(d -> leerCantidadResultados(d) > resultadosConFiltro);
    }

    public void prepararListadoConFiltroActivo() {
        abrirListadoLamparas();
        filtroActual = "marca";
        abrirFiltro(filtroActual);
        WebElement opcion = primeraOpcionDisponible(filtroActual);
        opcionFiltroActual = textoOpcion(opcion);
        clickEtiquetaOpcion(opcion);
        esperarOpcionSeleccionada(true);
    }

    public void cambiarOrdenamiento() {
        WebElement control = esperarClickable(BOTON_ORDENAMIENTO);
        String criterioActual = control.getText().trim();
        cerrarModalIaSiAparece();
        click(BOTON_ORDENAMIENTO);

        WebElement alternativa = wait.until(d -> {
            for (WebElement elemento : d.findElements(
                    By.cssSelector("select option, [role='option'], button, a"))) {
                try {
                    String texto = elemento.getText().trim();
                    String normalizado = normalizarTexto(texto);
                    String id = elemento.getAttribute("id");
                    if (elemento.isDisplayed() && elemento.isEnabled()
                            && !"a".equalsIgnoreCase(elemento.getTagName())
                            && (id == null || !id.startsWith("testId-Accordion-"))
                            && !texto.isEmpty()
                            && !"precio".equals(normalizado)
                            && !normalizarTexto(criterioActual).equals(normalizado)
                            && esCriterioOrdenamiento(normalizado)) {
                        return elemento;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // El menú de ordenamiento puede re-renderizar sus opciones al abrirse.
                }
            }
            return null;
        });

        criterioOrdenSeleccionado = alternativa.getText().trim();
        cerrarModalIaSiAparece();
        try {
            alternativa.click();
        } catch (ElementClickInterceptedException e) {
            clickPorJs(alternativa);
        }
        wait.until(d -> ordenamientoSeleccionado());
    }

    public boolean ordenamientoSeleccionado() {
        if (criterioOrdenSeleccionado == null) {
            return false;
        }
        return estaVisible(BOTON_ORDENAMIENTO, 3)
                && normalizarTexto(esperarVisible(BOTON_ORDENAMIENTO).getText())
                        .contains(normalizarTexto(criterioOrdenSeleccionado));
    }

    public void seleccionarLamparaDesdeListado() {
        abrirListadoLamparas();
        WebElement producto = wait.until(d -> d.findElements(PRODUCTOS).stream()
                .filter(WebElement::isDisplayed)
                .filter(elemento -> !elemento.getText().trim().isEmpty())
                .findFirst()
                .orElse(null));
        scrollHasta(producto);
        cerrarModalIaSiAparece();
        try {
            producto.click();
        } catch (ElementClickInterceptedException e) {
            clickPorJs(producto);
        }
        esperarVisible(TITULO_PRODUCTO);
    }

    public boolean nombreProductoVisible() {
        try {
            return wait.until(d -> d.findElements(TITULO_PRODUCTO).stream()
                    .anyMatch(titulo -> titulo.isDisplayed() && !titulo.getText().trim().isEmpty()));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean imagenProductoVisible() {
        try {
            WebElement imagen = esperarVisible(IMAGEN_PRINCIPAL_PRODUCTO);
            return Boolean.TRUE.equals(((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].complete && arguments[0].naturalWidth > 0;", imagen));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean informacionComercialVisible() {
        String texto = normalizarTexto(esperarVisible(BODY).getText());
        return texto.matches("(?s).*(\\$|\\bclp\\b|precio|disponibilidad|stock|despacho).*");
    }

    private void abrirListadoLamparas() {
        if (!driver.getCurrentUrl().toLowerCase(Locale.ROOT)
                .contains("/lista/cat40064/lamparas")) {
            WebElement enlace = wait.until(d -> d.findElements(LINK_CATEGORIA_LAMPARAS).stream()
                    .filter(WebElement::isDisplayed)
                    .filter(WebElement::isEnabled)
                    .findFirst()
                    .orElse(null));
            scrollHasta(enlace);
            cerrarModalIaSiAparece();
            try {
                enlace.click();
            } catch (ElementClickInterceptedException e) {
                clickPorJs(enlace);
            }
        }
        esperarVisible(LISTADO_LAMPARAS);
    }

    private int cantidadResultados() {
        String texto = esperarVisible(TOTAL_RESULTADOS).getText();
        Matcher matcher = Pattern.compile("Resultados\\s*\\(([\\d.]+)\\)").matcher(texto);
        if (!matcher.find()) {
            throw new IllegalStateException(
                    "No se pudo leer la cantidad de resultados desde: " + texto);
        }
        return Integer.parseInt(matcher.group(1).replace(".", ""));
    }

    private int leerCantidadResultados(WebDriver d) {
        for (WebElement elemento : d.findElements(TOTAL_RESULTADOS)) {
            Matcher matcher = Pattern.compile("Resultados\\s*\\(([\\d.]+)\\)")
                    .matcher(elemento.getText());
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1).replace(".", ""));
            }
        }
        return -1;
    }

    private boolean urlContieneFiltroActual() {
        String parametro = "marca".equals(filtroActual)
                ? "f.product.brandName="
                : "f.range.derived.price.search=";
        return driver.getCurrentUrl().contains(parametro);
    }

    private void abrirFiltro(String filtro) {
        By acordeon = By.id("testId-Accordion-" + nombreFiltro(filtro));
        WebElement boton = esperarClickable(acordeon);
        WebElement contenedor = boton.findElement(By.xpath(".."));
        boolean hayOpcionVisible = contenedor.findElements(By.cssSelector("label[for]"))
                .stream().anyMatch(WebElement::isDisplayed);
        if (!hayOpcionVisible) {
            cerrarModalIaSiAparece();
            boton.click();
        }
        wait.until(d -> opcionesFiltro(filtro).stream()
                .anyMatch(opcion -> opcion.findElement(By.xpath("ancestor::label[1]")).isDisplayed()));
    }

    private List<WebElement> opcionesFiltro(String filtro) {
        WebElement boton = driver.findElement(By.id("testId-Accordion-" + nombreFiltro(filtro)));
        return boton.findElement(By.xpath("..")).findElements(By.cssSelector("input[type='checkbox']"));
    }

    private WebElement primeraOpcionDisponible(String filtro) {
        return wait.until(d -> {
            for (WebElement opcion : opcionesFiltro(filtro)) {
                try {
                    WebElement etiqueta = opcion.findElement(By.xpath("ancestor::label[1]"));
                    if (opcion.isEnabled() && etiqueta.isDisplayed() && !textoOpcion(opcion).isEmpty()) {
                        return opcion;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // La lista de facetas puede actualizarse cuando se abre el acordeón.
                }
            }
            return null;
        });
    }

    private WebElement opcionPorTexto(String filtro, String texto) {
        String buscado = normalizarTexto(texto);
        return opcionesFiltro(filtro).stream()
                .filter(opcion -> normalizarTexto(textoOpcion(opcion)).equals(buscado))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No se encontró la opción de filtro '" + texto + "'."));
    }

    private String textoOpcion(WebElement opcion) {
        return opcion.findElement(By.xpath("ancestor::label[1]")).getText().trim();
    }

    private void clickEtiquetaOpcion(WebElement opcion) {
        WebElement etiqueta = opcion.findElement(By.xpath("ancestor::label[1]"));
        scrollHasta(etiqueta);
        cerrarModalIaSiAparece();
        clickPorJs(opcion);
    }

    private boolean opcionSeleccionada() {
        if (filtroActual == null || opcionFiltroActual == null) {
            return false;
        }
        try {
            return opcionPorTexto(filtroActual, opcionFiltroActual).isSelected();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private void esperarOpcionSeleccionada(boolean seleccionada) {
        wait.until(d -> opcionSeleccionada() == seleccionada
                && urlContieneFiltroActual() == seleccionada);
    }

    private String normalizarFiltro(String filtro) {
        String normalizado = normalizarTexto(filtro);
        if (!"marca".equals(normalizado) && !"precio".equals(normalizado)) {
            throw new IllegalArgumentException("Filtro no soportado: " + filtro);
        }
        return normalizado;
    }

    private String nombreFiltro(String filtro) {
        return "marca".equals(normalizarFiltro(filtro)) ? "Marca" : "Precio";
    }

    private boolean esCriterioOrdenamiento(String texto) {
        return texto.contains("precio") || texto.contains("mayor") || texto.contains("menor")
                || texto.contains("recomendado") || texto.contains("vendido")
                || texto.contains("novedad");
    }

    /*
     * La implementación anterior de filtros/ordenamiento/detalle se conserva debajo
     * como referencia histórica; las definiciones activas usan los métodos de arriba.
     */
/*
    public void listadoLamparas() {
        esperarVisible(LISTADO_LAMPARAS);
    }

    // ------------------------------------------------------------------ referencia

    /*
     * IMPLEMENTACIÓN PREVIA (comentada) de filtros, ordenamiento y detalle.
     * Se conserva como referencia para el agente de Copilot. NO descomentar tal cual:
     * usa Assert dentro de la página, driver.findElements directos y XPath genéricos.
     * Reimplementar con el estilo de esta clase (By privados + helpers de BasePage, sin asserts).
     */
/*
    public void seleccionarCategoria(String nombreCategoria) {
        Assert.assertEquals("La categoría esperada para este flujo es Iluminación",
                "iluminacion", normalizar(nombreCategoria));

        WebElement opcionCategoria = buscarOpcionVisible(nombreCategoria);
        if (opcionCategoria == null) {
            WebElement menuCategorias = buscarOpcionVisible("categorias");
            if (menuCategorias == null) {
                throw new AssertionError("No se encontró la opción Categorías en el menú principal");
            }

            new Actions(driver).moveToElement(menuCategorias).perform();
            menuCategorias.click();

            wait.until(d -> buscarOpcionVisible(nombreCategoria) != null);
            opcionCategoria = buscarOpcionVisible(nombreCategoria);
        }

        String urlAnterior = driver.getCurrentUrl();
        opcionCategoria.click();

        wait.until(d -> !d.getCurrentUrl().equals(urlAnterior)
                || d.findElements(By.cssSelector("h1")).stream()
                .anyMatch(titulo -> normalizar(titulo.getText()).contains("iluminacion")));
    }

  /*  private WebElement buscarOpcionVisible(String textoBuscado) {
        String textoNormalizado = normalizar(textoBuscado);
        return driver.findElements(MENU_CLICABLES).stream()
                .filter(WebElement::isDisplayed)
                .filter(WebElement::isEnabled)
                .filter(elemento -> normalizar(elemento.getText()).contains(textoNormalizado)
                        || normalizar(elemento.getAttribute("aria-label")).contains(textoNormalizado))
                .findFirst()
                .orElse(null);
    }



    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    public boolean muestraContextoIluminacion() {
        String titulo = driver.findElement(By.tagName("body")).getText()
                .toLowerCase(Locale.ROOT);
        return titulo.contains("iluminación") || titulo.contains("iluminacion");
    }

    public boolean disponeDeSubcategorias() {
        List<WebElement> enlaces = driver.findElements(By.xpath(
                "//a[.//img or @href][normalize-space(.)!='']"));
        return enlaces.stream().anyMatch(WebElement::isDisplayed);
    }

    public boolean imagenesPrincipalesCargadas() {
        List<WebElement> imagenes = driver.findElements(By.cssSelector("main img, [role='main'] img"));
        if (imagenes.isEmpty()) {
            imagenes = driver.findElements(By.cssSelector("img"));
        }
        return !imagenes.isEmpty() && imagenes.stream().filter(WebElement::isDisplayed)
                .allMatch(img -> {
                    Object naturalWidth = ((org.openqa.selenium.JavascriptExecutor) driver)
                            .executeScript("return arguments[0].naturalWidth", img);
                    return naturalWidth instanceof Number && ((Number) naturalWidth).intValue() > 0;
                });
    }

    public void accederAListadoDeLamparas(String filtro) {
        seleccionarEnlaceVisible("Lámparas", "Lamparas");
        abrirFiltro(filtro);
        Assert.assertFalse("No se encontró una opción de filtro con resultados para " + filtro,
                opcionesDeFiltro().isEmpty());
    }

    public void marcarOpcionDeFiltro() {
        List<WebElement> opciones = opcionesDeFiltro();
        Assert.assertFalse("No hay opciones seleccionables para el filtro", opciones.isEmpty());
        WebElement opcion = opciones.get(0);
        activeFilterOption = opcion;
        if (!estaSeleccionada(opcion)) {
            clickOpcion(opcion);
        }
        wait.until(d -> estaSeleccionada(activeFilterOption));
    }

    public boolean opcionActiva() {
        return activeFilterOption != null && estaSeleccionada(activeFilterOption);
    }

    public boolean catalogoActualizado() {
        return activeFilterOption != null && estaSeleccionada(activeFilterOption)
                && !driver.findElements(PRODUCT_LINKS).isEmpty();
    }

    public void desmarcarOpcionDeFiltro() {
        Assert.assertNotNull("Primero debe seleccionarse una opción de filtro", activeFilterOption);
        if (estaSeleccionada(activeFilterOption)) {
            clickOpcion(activeFilterOption);
        }
        wait.until(d -> !estaSeleccionada(activeFilterOption));
    }

    public boolean opcionNoSeleccionada() {
        return activeFilterOption != null && !estaSeleccionada(activeFilterOption);
    }

    public void prepararListadoConFiltroActivo() {
        seleccionarEnlaceVisible("Lámparas", "Lamparas");
        abrirFiltro("Marca");
        List<WebElement> opciones = opcionesDeFiltro();
        Assert.assertFalse("No hay opciones de Marca disponibles", opciones.isEmpty());
        activeFilterOption = opciones.get(0);
        if (!estaSeleccionada(activeFilterOption)) {
            clickOpcion(activeFilterOption);
        }
        wait.until(d -> estaSeleccionada(activeFilterOption));
    }

    public void cambiarOrdenamiento() {
        List<WebElement> selectores = driver.findElements(By.cssSelector("select"));
        for (WebElement selector : selectores) {
            if (selector.isDisplayed() && selector.isEnabled()) {
                List<WebElement> opciones = selector.findElements(By.tagName("option"));
                for (WebElement opcion : opciones) {
                    if (opcion.isEnabled() && !opcion.isSelected()) {
                        opcion.click();
                        selectedSortText = opcion.getText().trim();
                        return;
                    }
                }
            }
        }
        List<WebElement> botones = driver.findElements(By.xpath(
                "//*[self::button or self::a or @role='button']" +
                        "[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                        "'abcdefghijklmnopqrstuvwxyz'), 'ordenar') or " +
                        "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                        "'abcdefghijklmnopqrstuvwxyz'), 'recomendados')]"));
        clickFirstVisible(botones, "No se encontró un control de ordenamiento");
        List<WebElement> criterios = driver.findElements(By.xpath(
                "//*[self::button or self::a or @role='option']" +
                        "[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                        "'abcdefghijklmnopqrstuvwxyz'), 'precio') or " +
                        "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                        "'abcdefghijklmnopqrstuvwxyz'), 'mayor') or " +
                        "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                        "'abcdefghijklmnopqrstuvwxyz'), 'menor')]"));
        clickFirstVisible(criterios, "No se encontró un criterio alternativo de ordenamiento");
        selectedSortText = criterios.get(0).getText().trim();
    }

    public boolean ordenamientoSeleccionado() {
        if (selectedSortText == null) {
            return false;
        }
        return driver.findElements(By.xpath(
                "//*[self::option or self::button or self::a or @role='option']" +
                        "[normalize-space(.)=\"" + xpathLiteral(selectedSortText) + "\"]"))
                .stream().anyMatch(element ->
                        element.isDisplayed() && (element.isSelected()
                                || "true".equals(element.getAttribute("aria-selected"))
                                || "true".equals(element.getAttribute("aria-pressed"))
                                || element.getAttribute("class").toLowerCase(Locale.ROOT).contains("selected")));
    }

    public void abrirPrimerProductoDisponible() {
        List<WebElement> productos = driver.findElements(PRODUCT_LINKS);
        WebElement producto = productos.stream()
                .filter(WebElement::isDisplayed)
                .filter(element -> !element.getText().trim().isEmpty())
                .findFirst()
                .orElseThrow(() -> new AssertionError("No se encontró un producto disponible en el listado"));
        producto.click();
        wait.until(d -> !d.findElements(By.cssSelector("h1")).isEmpty());
    }

    public boolean nombreProductoVisible() {
        return driver.findElements(By.cssSelector("h1")).stream()
                .anyMatch(element -> element.isDisplayed() && !element.getText().trim().isEmpty());
    }

    public boolean imagenProductoVisible() {
        return driver.findElements(By.cssSelector("main img, [role='main'] img, img"))
                .stream().anyMatch(image -> image.isDisplayed()
                        && image.getAttribute("src") != null
                        && !image.getAttribute("src").trim().isEmpty());
    }

    public boolean informacionComercialVisible() {
        String texto = driver.findElement(By.tagName("body")).getText()
                .toLowerCase(Locale.ROOT);
        return texto.matches("(?s).*([$]|\\bclp\\b|precio|disponibilidad|stock|despacho).*");
    }

    private void seleccionarEnlaceVisible(String... textos) {
        for (String texto : textos) {
            List<WebElement> enlaces = driver.findElements(By.xpath(
                    "//*[self::a or self::button or @role='link']" +
                            "[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                            "'abcdefghijklmnopqrstuvwxyz'), '" +
                            texto.toLowerCase(Locale.ROOT) + "')]"));
            if (enlaces.stream().anyMatch(WebElement::isDisplayed)) {
                clickFirstVisible(enlaces, "No se encontró " + texto);
                return;
            }
        }
        Assert.fail("No se encontró un enlace visible a Lámparas");
    }

    private void abrirFiltro(String nombre) {
        List<WebElement> encabezados = driver.findElements(By.xpath(
                "//*[self::button or @role='button' or self::h2 or self::h3 or self::h4]" +
                        "[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', " +
                        "'abcdefghijklmnopqrstuvwxyz'), '" +
                        nombre.toLowerCase(Locale.ROOT) + "')]"));
        Assert.assertFalse("No se encontró el filtro " + nombre, encabezados.isEmpty());
        WebElement encabezado = encabezados.stream().filter(WebElement::isDisplayed)
                .findFirst().orElseThrow(() -> new AssertionError("El filtro " + nombre + " no está visible"));
        if (!"true".equals(encabezado.getAttribute("aria-expanded"))) {
            encabezado.click();
        }
    }

    private List<WebElement> opcionesDeFiltro() {
        return driver.findElements(By.xpath(
                "//label[.//input[@type='checkbox'] and normalize-space(.)!='']" +
                        " | //input[@type='checkbox'][not(ancestor::label)]"));
    }

    private String textoDeOpcion(WebElement opcion) {
        String texto = opcion.getText().trim();
        if (texto.isEmpty()) {
            texto = opcion.findElement(By.xpath("..")).getText().trim();
        }
        return texto;
    }

    private boolean estaSeleccionada(WebElement opcion) {
        if ("checkbox".equalsIgnoreCase(opcion.getAttribute("type"))) {
            return opcion.isSelected();
        }
        List<WebElement> checkboxes = opcion.findElements(By.cssSelector("input[type='checkbox']"));
        if (!checkboxes.isEmpty()) {
            return checkboxes.get(0).isSelected();
        }
        String ariaChecked = opcion.getAttribute("aria-checked");
        String clase = opcion.getAttribute("class");
        return "true".equals(ariaChecked)
                || (clase != null && clase.toLowerCase(Locale.ROOT).contains("selected"));
    }

    private void clickOpcion(WebElement opcion) {
        if ("checkbox".equalsIgnoreCase(opcion.getAttribute("type")) && !opcion.isDisplayed()) {
            opcion.findElement(By.xpath("..")).click();
        } else {
            opcion.click();
        }
    }

    private void clickFirstVisible(List<WebElement> elementos, String mensajeError) {
        WebElement elemento = elementos.stream().filter(WebElement::isDisplayed)
                .findFirst().orElseThrow(() -> new AssertionError(mensajeError));
        elemento.click();
    }

    private String xpathLiteral(String value) {
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }
}

   */
}

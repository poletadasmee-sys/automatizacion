package ObjectPage;

import Constant.Navegador;
import Control.BaseController;
import Control.DriverContext;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.time.Duration;

public class PisosPage extends BaseController {

    private static final String URL_SODIMAC = resolverUrlSodimac();
    private static final String SORT_TEXT_XPATH =
            "translate(normalize-space(concat(., ' ', @aria-label, ' ', @title)), " +
                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÜÑ', 'abcdefghijklmnopqrstuvwxyzáéíóúüñ')";
    private String criterioOrdenSeleccionado;

    private static String resolverUrlSodimac() {
        String url = System.getProperty("sodimac.baseUrl");
        if (url == null || url.isBlank()) {
            url = System.getenv("SODIMAC_BASE_URL");
        }
        if (url == null || url.isBlank()) {
            return "https://www.sodimac.cl/";
        }

        URI uri;
        try {
            uri = URI.create(url.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La URL configurada en SODIMAC_BASE_URL no es válida", e);
        }
        String scheme = uri.getScheme();
        if (uri.getHost() == null || scheme == null
                || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))
                || uri.getUserInfo() != null) {
            throw new IllegalArgumentException(
                    "SODIMAC_BASE_URL debe ser una URL HTTP(S) válida, sin credenciales");
        }
        return uri.toString();
    }

    private void asegurarDriver() {
        if (DriverContext.getDriver() == null) {
            DriverContext.setUp(Navegador.Chrome, URL_SODIMAC);
        }
        sincronizarDriver();
    }

    private String normalizarTexto(String texto) {
        return texto == null ? "" : texto.toLowerCase(Locale.ROOT).trim();
    }

    private String normalizarTextoBusqueda(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.toLowerCase(Locale.ROOT)
                .replace('á', 'a')
                .replace('é', 'e')
                .replace('í', 'i')
                .replace('ó', 'o')
                .replace('ú', 'u')
                .replace('ü', 'u')
                .replace('ñ', 'n')
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean coincideCriterioOrden(String criterioEsperado, String textoReal) {
        String esperado = normalizarTextoBusqueda(criterioEsperado);
        String real = normalizarTextoBusqueda(textoReal);
        if (esperado.isEmpty() || real.isEmpty()) {
            return false;
        }
        int direccionEsperada = direccionCriterioOrden(esperado);
        int direccionReal = direccionCriterioOrden(real);
        if (direccionEsperada != 0 || direccionReal != 0) {
            return direccionEsperada != 0 && direccionEsperada == direccionReal;
        }
        return esperado.equals(real) || real.contains(esperado);
    }

    private int direccionCriterioOrden(String criterio) {
        if (criterio.contains("mayor a menor") || criterio.contains("high to low")
                || criterio.contains("mayor precio") || criterio.contains("precio mayor")) {
            return -1;
        }
        if (criterio.contains("menor a mayor") || criterio.contains("low to high")
                || criterio.contains("menor precio") || criterio.contains("precio menor")) {
            return 1;
        }
        return 0;
    }

    private List<WebElement> buscarPorTexto(String texto) {
        asegurarDriver();
        String valor = normalizarTexto(texto);
        String xpath = "//a[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÜÑ', 'abcdefghijklmnopqrstuvwxyzáéíóúüñ'), '" + valor + "')] | //button[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÜÑ', 'abcdefghijklmnopqrstuvwxyzáéíóúüñ'), '" + valor + "')]";
        return driver.findElements(By.xpath(xpath));
    }

    private WebElement obtenerPrimerElementoVisible(List<WebElement> elementos) {
        for (WebElement elemento : elementos) {
            try {
                if (elemento != null && elemento.isDisplayed() && elemento.isEnabled()) {
                    return elemento;
                }
            } catch (Exception ignored) {
            }
        }
        return elementos.isEmpty() ? null : elementos.get(0);
    }

    private void hacerClickSeguro(WebElement elemento) {
        if (elemento == null) {
            return;
        }
        try {
            elemento.click();
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
        }
    }

    public boolean abrirPaginaPrincipal() {
        asegurarDriver();
        driver.get(URL_SODIMAC);
        String titulo = normalizarTexto(driver.getTitle());
        String contenido = normalizarTexto(driver.findElement(By.tagName("body")).getText());
        if (titulo.contains("cloudflare") || titulo.contains("attention required")
                || contenido.contains("sorry, you have been blocked")) {
            throw new IllegalStateException(
                    "El sitio bloqueó el acceso desde este entorno. Configure sodimac.baseUrl o "
                            + "SODIMAC_BASE_URL con la URL de un entorno de pruebas autorizado.");
        }
        return driver.getCurrentUrl().toLowerCase(Locale.ROOT).startsWith("http");
    }

    public void gestionarCookiesSiExiste() {
        asegurarDriver();
        List<WebElement> cookies = driver.findElements(By.xpath("//button[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÜÑ', 'abcdefghijklmnopqrstuvwxyzáéíóúüñ'), 'aceptar') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÜÑ', 'abcdefghijklmnopqrstuvwxyzáéíóúüñ'), 'cookies') or contains(@id, 'cookie') or contains(@class, 'cookie')]"));
        for (WebElement boton : cookies) {
            try {
                if (boton.isDisplayed() && boton.isEnabled()) {
                    hacerClickSeguro(boton);
                    break;
                }
            } catch (Exception ignored) {
            }
        }
    }

    public boolean seleccionarCategoria(String categoria) {
        List<WebElement> categoriaElegida = buscarPorTexto(categoria);
        WebElement el = obtenerPrimerElementoVisible(categoriaElegida);
        if (el == null) {
            return false;
        }
        hacerClickSeguro(el);
        return true;
    }

    public boolean validarContexto(String contexto) {
        asegurarDriver();
        String contenido = (driver.getTitle() + " " + driver.getPageSource()).toLowerCase(Locale.ROOT);
        return contenido.contains(normalizarTexto(contexto));
    }

    public boolean validarEnlacesRevestimientos() {
        asegurarDriver();
        List<WebElement> enlaces = driver.findElements(By.xpath("//a[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÜÑ', 'abcdefghijklmnopqrstuvwxyzáéíóúüñ'), 'piso') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÜÑ', 'abcdefghijklmnopqrstuvwxyzáéíóúüñ'), 'revestimiento')]"));
        return !enlaces.isEmpty();
    }

    public boolean seleccionarTipoDePisoVisible() {
        asegurarDriver();
        List<WebElement> tipos = driver.findElements(By.xpath("//a[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÜÑ', 'abcdefghijklmnopqrstuvwxyzáéíóúüñ'), 'piso') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚÜÑ', 'abcdefghijklmnopqrstuvwxyzáéíóúüñ'), 'revestimiento')]"));
        WebElement el = obtenerPrimerElementoVisible(tipos);
        if (el == null) {
            return false;
        }
        hacerClickSeguro(el);
        return true;
    }

    public boolean validarListadoRelacionado() {
        asegurarDriver();
        String contenido = driver.getPageSource().toLowerCase(Locale.ROOT);
        return contenido.contains("piso") || contenido.contains("revestimiento") || !driver.findElements(By.xpath("//*[@data-pod] | //article | //li[contains(@class,'product')]")).isEmpty();
    }

    public boolean validarRutaNavegacion() {
        asegurarDriver();
        String urlActual = driver.getCurrentUrl().toLowerCase(Locale.ROOT);
        return urlActual.contains("pisos") || urlActual.contains("revestimientos") || urlActual.contains("piso");
    }

    public boolean accederListadoConPreciosComparables() {
        asegurarDriver();
        if (buscarCajaOrdenVisible() == null) {
            if (!seleccionarTipoDePisoVisible()) {
                return false;
            }
        }
        return new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(d -> hayPreciosComparables() && buscarCajaOrdenVisible() != null);
    }

    private boolean hayPreciosComparables() {
        List<WebElement> precios = driver.findElements(
                By.xpath("//*[contains(text(), '$') or contains(text(), 'CLP') or contains(text(), 'UF')]"));
        return precios.size() >= 3;
    }

    public boolean seleccionarCriterio(String criterio) {
        asegurarDriver();

        List<WebElement> selects = driver.findElements(By.tagName("select"));
        for (WebElement select : selects) {
            try {
                if (!select.isDisplayed() || !select.isEnabled()) {
                    continue;
                }
                Select dropdown = new Select(select);
                for (WebElement opcion : dropdown.getOptions()) {
                    String texto = opcion.getText();
                    if (coincideCriterioOrden(criterio, texto)) {
                        dropdown.selectByVisibleText(texto);
                        criterioOrdenSeleccionado = criterio;
                        return true;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        WebElement cajaOrden = buscarCajaOrdenVisible();
        if (cajaOrden == null) {
            return false;
        }

        hacerClickSeguro(cajaOrden);
        WebDriverWait espera = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement opcion = espera.until(d -> buscarOpcionOrdenVisible(criterio));
        hacerClickSeguro(opcion);
        criterioOrdenSeleccionado = criterio;
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> selectorMuestraCriterio(criterio));
    }

    private WebElement buscarCajaOrdenVisible() {
        String tieneOrdenPor = "contains(" + SORT_TEXT_XPATH + ", 'ordenar por') or contains(" +
                SORT_TEXT_XPATH + ", 'sort by')";
        List<WebElement> candidatos = driver.findElements(By.xpath("//*[" + tieneOrdenPor + "]"));

        WebElement mejorCandidato = null;
        int mejorPrioridad = Integer.MAX_VALUE;
        int menorLongitud = Integer.MAX_VALUE;
        for (WebElement candidato : candidatos) {
            try {
                if (!candidato.isDisplayed() || !candidato.isEnabled()) {
                    continue;
                }
                String texto = normalizarTextoBusqueda(
                        candidato.getText() + " " + candidato.getAttribute("aria-label") + " " +
                                candidato.getAttribute("title"));
                if (!texto.contains("ordenar por") && !texto.contains("sort by")) {
                    continue;
                }
                String tag = candidato.getTagName().toLowerCase(Locale.ROOT);
                boolean interactivo = tag.equals("button") || tag.equals("select")
                        || "button".equalsIgnoreCase(candidato.getAttribute("role"))
                        || candidato.getAttribute("aria-haspopup") != null
                        || normalizarTextoBusqueda(candidato.getAttribute("class")).contains("sort")
                        || normalizarTextoBusqueda(candidato.getAttribute("class")).contains("select");
                int prioridad = interactivo ? 0 : texto.contains("recomendados") ? 1 : 2;
                if (prioridad < mejorPrioridad
                        || prioridad == mejorPrioridad && texto.length() < menorLongitud) {
                    mejorCandidato = candidato;
                    mejorPrioridad = prioridad;
                    menorLongitud = texto.length();
                }
            } catch (Exception ignored) {
            }
        }
        return mejorCandidato;
    }

    private WebElement buscarOpcionOrdenVisible(String criterio) {
        String textoOrden = "contains(" + SORT_TEXT_XPATH + ", 'precio') and " +
                "(contains(" + SORT_TEXT_XPATH + ", 'menor') or contains(" + SORT_TEXT_XPATH + ", 'mayor') " +
                "or contains(" + SORT_TEXT_XPATH + ", 'low to high') or contains(" + SORT_TEXT_XPATH + ", 'high to low'))";
        List<WebElement> candidatos = driver.findElements(By.xpath(
                "//*[self::option or self::button or self::a or self::li or @role='option' or " +
                        "@role='menuitem' or @tabindex or self::div or self::span][" + textoOrden + "]"));

        WebElement mejorCandidato = null;
        int mejorPrioridad = Integer.MAX_VALUE;
        int menorLongitud = Integer.MAX_VALUE;
        for (WebElement candidato : candidatos) {
            try {
                String texto = candidato.getText();
                if (!candidato.isDisplayed() || !candidato.isEnabled()
                        || !coincideCriterioOrden(criterio, texto) || texto.isEmpty()) {
                    continue;
                }
                String tag = candidato.getTagName().toLowerCase(Locale.ROOT);
                boolean interactivo = tag.equals("option") || tag.equals("button") || tag.equals("a")
                        || "option".equalsIgnoreCase(candidato.getAttribute("role"))
                        || "menuitem".equalsIgnoreCase(candidato.getAttribute("role"))
                        || candidato.getAttribute("tabindex") != null;
                int prioridad = interactivo ? 0 : tag.equals("li") ? 1 : tag.equals("div") ? 2 : 3;
                if (prioridad < mejorPrioridad
                        || prioridad == mejorPrioridad && texto.length() < menorLongitud) {
                    mejorCandidato = candidato;
                    mejorPrioridad = prioridad;
                    menorLongitud = texto.length();
                }
            } catch (Exception ignored) {
            }
        }
        return mejorCandidato;
    }

    public boolean validarSelectorOrden(String criterio) {
        asegurarDriver();
        return selectorMuestraCriterio(criterio);
    }

    private boolean selectorMuestraCriterio(String criterio) {
        List<WebElement> selects = driver.findElements(By.tagName("select"));
        for (WebElement select : selects) {
            try {
                if (select.isDisplayed() && coincideCriterioOrden(
                        criterio, new Select(select).getFirstSelectedOption().getText())) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        String textoVisible = normalizarTextoBusqueda(
                driver.findElement(By.tagName("body")).getText());
        int inicioOrden = textoVisible.lastIndexOf("ordenar por");
        if (inicioOrden < 0) {
            return false;
        }
        int finOrden = Math.min(textoVisible.length(), inicioOrden + 120);
        return coincideCriterioOrden(criterio, textoVisible.substring(inicioOrden, finOrden));
    }

    public boolean validarPrecioOrdenado(String direccion) {
        asegurarDriver();
        int direccionSeleccionada = direccionCriterioOrden(
                normalizarTextoBusqueda(criterioOrdenSeleccionado));
        int direccionEsperada = "ascendente".equalsIgnoreCase(direccion) ? 1
                : "descendente".equalsIgnoreCase(direccion) ? -1 : 0;
        return direccionEsperada != 0 && direccionSeleccionada == direccionEsperada
                && validarSelectorOrden(criterioOrdenSeleccionado);
    }

    public boolean identificarProductoVisible() {
        asegurarDriver();
        List<WebElement> nombres = driver.findElements(By.xpath("//h1 | //h2 | //h3 | //h4 | //* [contains(@class, 'pod-subTitle')] | //* [contains(@class, 'product-name')]"));
        for (WebElement nombre : nombres) {
            if (nombre.getText() != null && !nombre.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean abrirFichaProducto() {
        asegurarDriver();
        List<WebElement> enlaces = driver.findElements(By.xpath("//a[contains(@href, 'producto') or contains(@href, 'piso') or contains(@href, 'revestimiento')]"));
        WebElement el = obtenerPrimerElementoVisible(enlaces);
        if (el == null) {
            return false;
        }
        hacerClickSeguro(el);
        return true;
    }

    public boolean validarNombreFicha() {
        asegurarDriver();
        List<WebElement> titulos = driver.findElements(By.xpath("//h1 | //h2 | //h3 | //h4"));
        for (WebElement titulo : titulos) {
            String texto = titulo.getText() == null ? "" : titulo.getText().trim();
            if (!texto.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean validarImagenYPrecio() {
        asegurarDriver();
        List<WebElement> imagenes = driver.findElements(By.tagName("img"));
        List<WebElement> precios = driver.findElements(By.xpath("//*[contains(text(), '$') or contains(text(), 'CLP') or contains(text(), 'UF')]"));
        return !imagenes.isEmpty() && !precios.isEmpty();
    }
}

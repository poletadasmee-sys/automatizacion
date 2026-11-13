package ObjectPage;

import Control.BaseController;
import Control.DriverContext;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BanoPage extends BaseController {

    @FindBy(xpath = "//*[normalize-space()='Menú']")
    private WebElement menuPrincipal;

    @FindBy(xpath = "//span[normalize-space()='Baño']")
    private WebElement categoriaBano;

    @FindBy(xpath = "//span[contains(normalize-space(),'Griferías')]")
    private WebElement categoriaGriferias;

    @FindBy(id = "testId-accept-cookies-btn")
    private WebElement btnAceptarCookies;

    @FindBy(css = "button.exp1416b-dismiss")
    private WebElement btnOmitirAsesorIA;

    @FindBy(id = "testId-modal-close")
    private WebElement btnCerrarUbicacion;

    @FindBy(xpath = "(//input[@type='checkbox'])[1]")
    private WebElement opcionPrimerFiltro;

    @FindBy(xpath = "(//input[@type='checkbox'])[2]")
    private WebElement opcionSegundoFiltro;

    @FindBy(xpath = "//button[contains(normalize-space(),'Limpiar') or contains(normalize-space(),'Borrar')] | //span[contains(normalize-space(),'Limpiar')]/ancestor::button")
    private WebElement btnLimpiarFiltros;

    @FindBy(css = "[data-testid='pod-card'], div[id*='testId-pod-']")
    private java.util.List<WebElement> tarjetasProductos;

    public void abrirMenuPrincipal() {

        try {

            visualizarElemento(menuPrincipal, 10);

            System.out.println("Click Menú");

            menuPrincipal.click();
            Thread.sleep(1000);

        } catch (Exception e) {

            Assert.fail(
                    "Error al abrir menú principal: "
                            + e.getMessage()
            );
        }
    }

    public void gestionarPopupsIniciales() {

        try {

            if (visualizarElemento(btnAceptarCookies, 3)) {

                btnAceptarCookies.click();
                System.out.println("Cookies cerradas");

            }

        } catch (Exception e) {

            System.out.println("Cookies no visibles");

        }

        try {

            if (visualizarElemento(btnCerrarUbicacion, 3)) {

                btnCerrarUbicacion.click();
                Thread.sleep(1000);
                System.out.println("Popup ubicación cerrado");

            }

        } catch (Exception e) {

            System.out.println("Popup ubicación no visible");

        }

        try {

            if (visualizarElemento(btnOmitirAsesorIA, 3)) {

                btnOmitirAsesorIA.click();
                Thread.sleep(1000);
                System.out.println("AsesorIA cerrada");

            }

        } catch (Exception e) {

            System.out.println("AsesorIA no visible");

        }
    }

    public void seleccionarCategoriaBano() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(10));
            WebElement banoElem = wait.until(ExpectedConditions.visibilityOf(categoriaBano));

            // Solo posicionar el mouse (hover) para desplegar el submenú sin cerrar el panel
            org.openqa.selenium.interactions.Actions actions =
                    new org.openqa.selenium.interactions.Actions(DriverContext.getDriver());
            actions.moveToElement(banoElem).perform();

            wait.until(ExpectedConditions.visibilityOf(categoriaGriferias));
            System.out.println("Submenú de Baño desplegado correctamente");
        } catch (Exception e) {
            Assert.fail("Error al desplegar submenú de Baño: " + e.getMessage());
        }
    }

    public void seleccionarGriferias() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(10));
            WebElement griferias = wait.until(ExpectedConditions.elementToBeClickable(categoriaGriferias));

            // Forzar clic por JS por si hay superposición del menú
            ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                    .executeScript("arguments[0].click();", griferias);

            System.out.println("Click Griferías ejecutado por JS");
        } catch (Exception e) {
            Assert.fail("Error al seleccionar categoría Griferías: " + e.getMessage());
        }
    }


    public void validarPaginaGriferias() {

        try {

            String urlActual =
                    DriverContext.getDriver().getCurrentUrl();

            System.out.println("URL actual: " + urlActual);

            Assert.assertTrue(
                    "No se ingresó al listado de Griferías. URL actual: " + urlActual,
                    urlActual.contains("Griferia-para-Bano")
            );

        } catch (Exception e) {

            Assert.fail(
                    "Error validando página de Griferías: "
                            + e.getMessage()
            );
        }
    }

    public void validarCategoriaBano() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(8));
            boolean menuVisible = false;
            try {
                menuVisible = wait.until(ExpectedConditions.visibilityOf(categoriaGriferias)).isDisplayed();
            } catch (Exception ignored) {}

            String urlActual = DriverContext.getDriver().getCurrentUrl().toLowerCase();
            boolean enPaginaBano = urlActual.contains("bano") || urlActual.contains("ba%c3%b1o");

            Assert.assertTrue(
                    "No se visualiza ni el submenú ni la página de la categoría Baño. URL: " + urlActual,
                    menuVisible || enPaginaBano
            );
            System.out.println("Contexto de la categoría Baño validado correctamente");
        } catch (Exception e) {
            Assert.fail("Error validando el contexto de la categoría Baño: " + e.getMessage());
        }
    }

    //MÉTODOS PARA @filtros

    public void validarListadoConFiltros() {
        try {
            seleccionarGriferias();
            validarPaginaGriferias();
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("(//input[@type='checkbox'])[2]")
            ));
            Assert.assertFalse("No se encontraron productos en el listado", tarjetasProductos.isEmpty());
        } catch (Exception e) {
            Assert.fail("Error al validar listado con filtros disponibles: " + e.getMessage());
        }
    }

    public void seleccionarPrimerFiltro() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("(//input[@type='checkbox'])[1]")
            ));
            // Clic por JS para evitar ElementClickInterceptedException si el input tiene un label/span encima
            ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                    .executeScript("arguments[0].click();", opcionPrimerFiltro);
            Thread.sleep(2000); // Espera actualización de grilla
        } catch (Exception e) {
            Assert.fail("Error al seleccionar el primer filtro: " + e.getMessage());
        }
    }

    public void seleccionarSegundoFiltro() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("(//input[@type='checkbox'])[2]")
            ));
            ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                    .executeScript("arguments[0].click();", opcionSegundoFiltro);
            Thread.sleep(2000);
        } catch (Exception e) {
            Assert.fail("Error al seleccionar el segundo filtro: " + e.getMessage());
        }
    }

    public void validarFiltrosActivos() {
        Assert.assertTrue("El primer filtro no quedó seleccionado", opcionPrimerFiltro.isSelected());
        Assert.assertTrue("El segundo filtro no quedó seleccionado", opcionSegundoFiltro.isSelected());
    }

    public void validarResultadosFiltrados() {
        Assert.assertFalse("No se muestran tarjetas tras aplicar los dos filtros", tarjetasProductos.isEmpty());
    }

    public void limpiarFiltros() {
        try {
            Thread.sleep(1000);
            // Buscar el elemento de forma dinámica para evitar referencias estancadas por AJAX
            WebElement chk1 = DriverContext.getDriver().findElement(org.openqa.selenium.By.xpath("(//input[@type='checkbox'])[1]"));
            if (chk1.isSelected()) {
                ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                        .executeScript("arguments[0].click();", chk1);
                Thread.sleep(1500);
            }

            WebElement chk2 = DriverContext.getDriver().findElement(org.openqa.selenium.By.xpath("(//input[@type='checkbox'])[2]"));
            if (chk2.isSelected()) {
                ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                        .executeScript("arguments[0].click();", chk2);
                Thread.sleep(1500);
            }

            System.out.println("Filtros limpiados exitosamente");
        } catch (Exception e) {
            Assert.fail("Error al limpiar filtros: " + e.getMessage());
        }
    }

    public void validarFiltrosLimpios() {
        try {
            Thread.sleep(1000);
            // Re-localizar dinámicamente para verificar el estado real actual
            WebElement chk1 = DriverContext.getDriver().findElement(org.openqa.selenium.By.xpath("(//input[@type='checkbox'])[1]"));
            WebElement chk2 = DriverContext.getDriver().findElement(org.openqa.selenium.By.xpath("(//input[@type='checkbox'])[2]"));

            Assert.assertFalse("El primer filtro sigue activo", chk1.isSelected());
            Assert.assertFalse("El segundo filtro sigue activo", chk2.isSelected());
            System.out.println("Filtros validados como desmarcados correctamente");
        } catch (Exception e) {
            Assert.fail("Error al validar filtros limpios: " + e.getMessage());
        }
    }

    // ==========================================
    // VARIABLES DE ESTADO PARA @detalle Y @navegacion
    // ==========================================
    private String srcImagenInicial;
    private String textoBreadcrumbSeleccionado;

    // ==========================================
    // MÉTODOS PARA @detalle (GALERÍA PDP)
    // ==========================================

    public void abrirProductoConGaleria() {
        try {
            seleccionarGriferias();
            validarPaginaGriferias();

            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(15));
            org.openqa.selenium.By selectorProducto = org.openqa.selenium.By.xpath(
                    "//div[contains(@class,'pod')]//a | //article//a | //div[contains(@id,'testId-pod')]//a | (//section//a[contains(@href,'/p/')])[1]"
            );

            WebElement primerProducto = wait.until(ExpectedConditions.elementToBeClickable(selectorProducto));

            ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                    .executeScript("arguments[0].scrollIntoView(true);", primerProducto);
            Thread.sleep(1000);

            ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                    .executeScript("arguments[0].click();", primerProducto);

            wait.until(ExpectedConditions.presenceOfElementLocated(org.openqa.selenium.By.tagName("h1")));
            Thread.sleep(2000);

            System.out.println("Ficha de producto abierta correctamente desde el listado");
        } catch (Exception e) {
            Assert.fail("Error al abrir la ficha del producto: " + e.getMessage());
        }
    }

    private String obtenerUrlImagen(WebElement img) {
        String url = img.getAttribute("currentSrc");
        if (url == null || url.isEmpty()) url = img.getAttribute("src");
        if (url == null || url.isEmpty()) url = img.getAttribute("srcset");
        return url != null ? url.trim() : "";
    }

    public void seleccionarMiniaturaGaleria() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(10));

            // Obtener todas las imágenes de la página
            java.util.List<WebElement> todasImgs = DriverContext.getDriver().findElements(org.openqa.selenium.By.tagName("img"));

            WebElement imgPrincipal = null;
            for (WebElement img : todasImgs) {
                if (img.isDisplayed() && img.getSize().getWidth() >= 200) {
                    String url = obtenerUrlImagen(img).toLowerCase();
                    // Evitar banners promocionales o carruseles institucionales de la cabecera
                    if (!url.contains("renovar") && !url.contains("hero") && !url.contains("banner") && !url.contains("logo")) {
                        imgPrincipal = img;
                        break;
                    }
                }
            }

            Assert.assertNotNull("No se encontró la imagen principal del producto en la ficha", imgPrincipal);
            srcImagenInicial = obtenerUrlImagen(imgPrincipal);
            System.out.println("Imagen principal del producto inicial: " + srcImagenInicial);

            // Buscar miniaturas (imágenes pequeñas de galería de producto entre 30px y 140px)
            java.util.List<WebElement> miniaturas = new java.util.ArrayList<>();
            for (WebElement img : todasImgs) {
                int w = img.getSize().getWidth();
                int h = img.getSize().getHeight();
                String url = obtenerUrlImagen(img).toLowerCase();
                if (img.isDisplayed() && w >= 30 && w <= 140 && h >= 30 && h <= 140
                        && !url.contains("renovar") && !url.contains("hero") && !url.contains("icon")) {
                    miniaturas.add(img);
                }
            }

            Assert.assertTrue("El producto abierto no tiene múltiples miniaturas en la galería (encontradas: " + miniaturas.size() + ")", miniaturas.size() > 1);

            // Seleccionar la segunda miniatura (o su botón contenedor si aplica)
            WebElement miniaturaTarget = miniaturas.get(1);
            WebElement elementoClic = miniaturaTarget;
            try {
                WebElement padreBtn = miniaturaTarget.findElement(org.openqa.selenium.By.xpath("./ancestor::button[1]"));
                if (padreBtn.isDisplayed()) {
                    elementoClic = padreBtn;
                }
            } catch (Exception ignored) {}

            ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                    .executeScript("arguments[0].scrollIntoView({block: 'center'});", elementoClic);
            Thread.sleep(500);

            try {
                elementoClic.click();
            } catch (Exception e) {
                ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                        .executeScript("arguments[0].click();", elementoClic);
            }

            Thread.sleep(2000); // Esperar actualización de React
            System.out.println("Segunda miniatura seleccionada correctamente");
        } catch (Exception e) {
            Assert.fail("Error al seleccionar miniatura de galería: " + e.getMessage());
        }
    }

    public void validarCambioImagenPrincipal() {
        try {
            String srcImagenActual = "";
            long startTime = System.currentTimeMillis();

            // Esperar activamente hasta que el src de la imagen principal cambie respecto al inicial
            while (System.currentTimeMillis() - startTime < 8000) {
                java.util.List<WebElement> todasImgs = DriverContext.getDriver().findElements(org.openqa.selenium.By.tagName("img"));
                for (WebElement img : todasImgs) {
                    if (img.isDisplayed() && img.getSize().getWidth() >= 200) {
                        String url = obtenerUrlImagen(img).toLowerCase();
                        if (!url.contains("renovar") && !url.contains("hero") && !url.contains("banner") && !url.contains("logo")) {
                            srcImagenActual = obtenerUrlImagen(img);
                            break;
                        }
                    }
                }
                if (!srcImagenActual.isEmpty() && !srcImagenActual.equals(srcImagenInicial)) {
                    break;
                }
                Thread.sleep(500);
            }

            System.out.println("Imagen principal actual tras clic: " + srcImagenActual);
            Assert.assertFalse("No se pudo obtener la URL de la imagen principal actualizada", srcImagenActual.isEmpty());
            Assert.assertNotEquals("La imagen principal no cambió al seleccionar la miniatura", srcImagenInicial, srcImagenActual);
            System.out.println("Imagen principal actualizada visualmente con éxito");
        } catch (Exception e) {
            Assert.fail("Error validando cambio de imagen principal: " + e.getMessage());
        }
    }

    public void validarNombreProductoVisible() {
        try {
            WebElement tituloProducto = DriverContext.getDriver().findElement(org.openqa.selenium.By.tagName("h1"));
            Assert.assertTrue("El nombre del producto no está visible", tituloProducto.isDisplayed());
            Assert.assertFalse("El nombre del producto está vacío", tituloProducto.getText().trim().isEmpty());
            System.out.println("Nombre del producto visible: " + tituloProducto.getText().trim());
        } catch (Exception e) {
            Assert.fail("Error validando nombre del producto: " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS PARA @navegacion (BREADCRUMBS)
    // ==========================================

    private java.util.List<WebElement> obtenerEnlacesBreadcrumb() {
        java.util.List<WebElement> enlaces = DriverContext.getDriver().findElements(
                org.openqa.selenium.By.xpath(
                        "//nav[contains(@aria-label,'breadcrumb') or contains(@aria-label,'Breadcrumb') or contains(@aria-label,'ruta')]//a | " +
                                "//*[contains(@class,'breadcrumb') or contains(@id,'breadcrumb') or contains(@data-testid,'breadcrumb')]//a | " +
                                "//ol//a | //ul//a[not(ancestor::header) and not(ancestor::footer)] | " +
                                "//div[contains(@class,'category') or contains(@class,'department')]//a"
                )
        );

        java.util.List<WebElement> visibles = new java.util.ArrayList<>();
        for (WebElement a : enlaces) {
            try {
                if (a.isDisplayed() && !a.getText().trim().isEmpty() && a.getLocation().getY() < 600) {
                    String href = a.getAttribute("href");
                    if (href != null && !href.contains("/product/") && !visibles.contains(a)) {
                        visibles.add(a);
                    }
                }
            } catch (Exception ignored) {}
        }
        return visibles;
    }

    public void abrirFichaConBreadcrumbs() {
        abrirProductoConGaleria();
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(10));
            wait.until(driver -> obtenerEnlacesBreadcrumb().size() >= 1);
        } catch (Exception ignored) {}

        java.util.List<WebElement> breadcrumbs = obtenerEnlacesBreadcrumb();
        System.out.println("Enlaces de ruta de navegación detectados: " + breadcrumbs.size());
        for (WebElement b : breadcrumbs) {
            System.out.println(" - Breadcrumb: " + b.getText().trim() + " -> " + b.getAttribute("href"));
        }

        Assert.assertTrue("No se visualiza ninguna ruta de navegación o enlace de categoría en la ficha", breadcrumbs.size() >= 1);
    }

    public void hacerClicEnBreadcrumbAnterior() {
        try {
            java.util.List<WebElement> breadcrumbs = obtenerEnlacesBreadcrumb();
            Assert.assertFalse("No hay enlaces de navegación disponibles", breadcrumbs.isEmpty());

            WebElement enlaceAnterior = breadcrumbs.get(breadcrumbs.size() - 1);
            textoBreadcrumbSeleccionado = enlaceAnterior.getText().trim();
            String hrefDestino = enlaceAnterior.getAttribute("href");
            System.out.println("Haciendo clic en breadcrumb: " + textoBreadcrumbSeleccionado + " (" + hrefDestino + ")");

            ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                    .executeScript("arguments[0].scrollIntoView({block: 'center'});", enlaceAnterior);
            Thread.sleep(500);
            ((org.openqa.selenium.JavascriptExecutor) DriverContext.getDriver())
                    .executeScript("arguments[0].click();", enlaceAnterior);

            Thread.sleep(2500);
        } catch (Exception e) {
            Assert.fail("Error al navegar mediante breadcrumb: " + e.getMessage());
        }
    }

    public void validarRetornoContextoBreadcrumb() {
        String urlActual = DriverContext.getDriver().getCurrentUrl().toLowerCase();
        String tituloPagina = DriverContext.getDriver().getTitle().toLowerCase();

        System.out.println("URL tras clic en breadcrumb: " + urlActual);
        Assert.assertFalse("Sigue en la ficha del producto y no volvió atrás", urlActual.contains("/product/"));
        Assert.assertTrue(
                "No se abandonó la ficha del producto correctamente. URL actual: " + urlActual,
                !urlActual.contains("/product/")
        );
        System.out.println("Retorno de contexto validado correctamente: " + urlActual);
    }

    public void validarRetornoCategoriaBreadcrumb() {
        String urlActual = DriverContext.getDriver().getCurrentUrl().toLowerCase();

        System.out.println("URL tras clic en breadcrumb: " + urlActual);
        Assert.assertFalse("Sigue en la ficha del producto y no volvió atrás", urlActual.contains("/product/"));
        Assert.assertTrue(
                "No se abandonó la ficha del producto correctamente. URL actual: " + urlActual,
                !urlActual.contains("/product/")
        );
        System.out.println("Retorno de contexto validado correctamente: " + urlActual);
    }
}
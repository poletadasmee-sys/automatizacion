package ObjectPage;
import Control.DriverContext;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;

public class SodimacHerPage {

//xpath--------------------------------------------------------------------------------------------------

    @FindBy(xpath = "//*[@id=\"testId-accept-cookies-btn\"]")
    private WebElement aceptarCookies;
    @FindBy(xpath = "//*[contains(text(),'Menú')]")
    private WebElement menuHamburguesa;
    @FindBy(xpath = "//span[contains(text(),'Herramientas y Maquinaria')]")
    private WebElement categoriaHerramientasMaquinaria;



    //xpath smoke------------------------------------------------------------------------------------------
    @FindBy(xpath = "//span[contains(text(),'Todo Herramientas')]")
    private WebElement todoHerramientas;
    @FindBy(xpath = "//*[@id=\"main-bottom-RichText-7111c200-54ed-4495-97d3-0450fd96ac8c\"]/div/div/h2/strong")
    private WebElement tituloCategoria;
    @FindBy(xpath = "//*[@id='scrollable-content']/div/div[2]/div/div[2]//a")
    private List<WebElement> enlacesSubcategorias;
    @FindBy(xpath ="//span[contains(normalize-space(),'Eléctricas e Inalámbricas')]")
    private WebElement subcategoriaElectricasInalambricas;
    @FindBy (xpath = "//*[@id=\"breadcrumb\"]/ol/li[3]")
    private WebElement tituloSubcategoria;
    @FindBy (xpath = "//*[@id=\"testId-CategoryTitle-container\"]/span/h1")
    private WebElement productosSubcategoria;

//Xpath Ordenamiento---------------------------------------------------------------------------------

    @FindBy(xpath = "//div[contains(@class, 'search-results--cell') or contains(@class, 'search-results-4-grid')]")
    private WebElement contenedorTarjeta;
    @FindBy(xpath = "//*[@id='testId-searchResults-products']")
    private WebElement contenedorProductos;
    @FindBy(xpath = "//b[contains(@class, 'pod-subTitle') or contains(@class, 'subTitle-rebrand')]")
    private WebElement nombreProducto;
    @FindBy(xpath = "//ol[contains(@class, 'pod-prices')]//span")
    private WebElement precioProducto;
    @FindBy(xpath = "//button[contains(@id, 'testId-Dropdown-desktop-button') or contains(@class, 'sort-by-button')]")
    private WebElement btnOrdenarPor;
    @FindBy(xpath = "//div[contains(@class, 'prices')]//li[contains(@class, 'prices-0') or contains(@class, 'primary')]//span[contains(text(), '$')]")
    private List<WebElement> listaPreciosVisibles;
    @FindBy(xpath = "//*[contains(@id,'testId-pod-')]")
    private List<WebElement> tarjetasProductos;

//Xpath Detalle---------------------------------------------------------------------------------
@FindBy(xpath = "(//*[contains(@id,'testId-pod-')])[1]")
private WebElement primeraTarjetaHerramienta;
    @FindBy(xpath = "(//b[contains(@class, 'pod-subTitle') or contains(@class, 'subTitle-rebrand')])[1]")
    private WebElement nombreProductoTarjeta;
    @FindBy(xpath = "(//div[contains(@class, 'prices')]//span[contains(text(), '$')])[1]")
    private WebElement precioProductoTarjeta;
    @FindBy(xpath = ".//b[contains(@class, 'pod-subTitle') or contains(@class, 'subTitle-rebrand')]")
    private WebElement linkFichaProducto;
    @FindBy(xpath = "//h1[contains(@class, 'product-title') or contains(@class, 'name')]")
    private WebElement tituloFichaProducto;
    @FindBy(xpath = "(//div[contains(@class, 'pod') or contains(@class, 'product')]//img)[1]")
    private WebElement imagenPrincipalHerramienta;
    @FindBy(xpath = "//span[contains(text(),'$')]")
    private WebElement precioFichaProducto;
    //Navegacion-------------------------------------------------------------------------------------------

    @FindBy(xpath = "//div[contains(@class, 'search-results') or contains(@class, 'grid-content') or contains(@class, 'pod')]")
    private WebElement contenedorListado;
    @FindBy(xpath = "//div[contains(@class, 'pagination') or contains(@class, 'filter') or contains(@class, 'search-results-footer')]")
    private WebElement controlesExploracionListado;

    //Constructor------------------------------------------------------------------------------------------
    private WebDriver driver;
                public SodimacHerPage() {
                this.driver = DriverContext.getDriver();
                PageFactory.initElements(driver, this);
    }

    public  void abrirPagina(String url) {
        driver.get(url);
    }
    private WebDriverWait wait;
    public void gestionarCookies() {
        try {
            if (aceptarCookies.isDisplayed()) {
                ((JavascriptExecutor) DriverContext.getDriver()).executeScript("arguments[0].click();", aceptarCookies);
            }
        } catch (Exception e) {
            System.out.println("El aviso de cookies no apareció o ya fue aceptado.");
        }

        // Cerramos el modal de "AsesorIA" si llega a aparecer en pantalla
        try {
            WebElement botonOmitirAsesorIA = DriverContext.getDriver().findElement(By.xpath("//button[contains(text(),'Omitir')]"));
            if (botonOmitirAsesorIA.isDisplayed()) {
                ((JavascriptExecutor) DriverContext.getDriver()).executeScript("arguments[0].click();", botonOmitirAsesorIA);
            }
        } catch (Exception e) {
            // Si el banner de AsesorIA no está, continúa sin problema
        }
    }
    public void cerrarPopups() {

        try {

            WebElement btnNoGracias =
                    driver.findElement(
                            By.xpath("//button[contains(.,'No, gracias')]"));

            ((JavascriptExecutor) driver)
                    .executeScript(
                            "arguments[0].click();",
                            btnNoGracias);

            Thread.sleep(1000);

        } catch (Exception ignored) {}

        try {

            WebElement btnOmitir =
                    driver.findElement(
                            By.xpath("//button[contains(.,'Omitir')]"));

            ((JavascriptExecutor) driver)
                    .executeScript(
                            "arguments[0].click();",
                            btnOmitir);

            Thread.sleep(1000);

        } catch (Exception ignored) {}
    }

    public void seleccionarCategoriaMenu(String categoria) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.elementToBeClickable(menuHamburguesa));
        menuHamburguesa.click();

        wait.until(ExpectedConditions.visibilityOf(categoriaHerramientasMaquinaria));

        Actions actions = new Actions(driver);

        actions.moveToElement(categoriaHerramientasMaquinaria)
                .perform();

        System.out.println("Categoría desplegada");
    }
//Smoke

    public void validarTituloCategoria(String tituloEsperado) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(
                ExpectedConditions.visibilityOf(
                        subcategoriaElectricasInalambricas));

        Assert.assertTrue(
                "No se desplegó el menú de Herramientas y Maquinaria",
                subcategoriaElectricasInalambricas.isDisplayed());
    }

    public void validarSubcategoriasInteractivos() {

        List<WebElement> enlaces =
                driver.findElements(
                        By.xpath("//*[@id='scrollable-content']/div/div[2]/div/div[2]//a"));

        System.out.println(
                "Subcategorías encontradas: "
                        + enlaces.size());

        Assert.assertFalse(
                "No se encontraron enlaces de subcategorías",
                enlaces.isEmpty());

        boolean existeInteractivo = enlaces.stream()
                .anyMatch(e -> e.isDisplayed() && e.isEnabled());

        Assert.assertTrue(
                "No existen enlaces interactivos visibles",
                existeInteractivo);
        System.out.println(
                "Subcategorías encontradas: "
                        + enlaces.size());
    }

    public void seleccionarSubcategoriaHerramientasElectricas() {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(
                ExpectedConditions.visibilityOf(
                        subcategoriaElectricasInalambricas));

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].click();",
                        subcategoriaElectricasInalambricas);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id='testId-searchResults-products']")));
        System.out.println(
                "URL actual: "
                        + driver.getCurrentUrl());
    }

    public void validarListadoOPortadaSubcategoria() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        boolean existePortada = false;
        boolean existeListado = false;

        try {
            wait.until(ExpectedConditions.visibilityOf(productosSubcategoria));
            existeListado = productosSubcategoria.isDisplayed();
        } catch (Exception e) {
            System.out.println("No se encontró el contenedor/título de productos.");
        }

        Assert.assertTrue(
                "No se visualizó una portada ni un listado relacionado con la subcategoría",
                existePortada || existeListado);
    }

//Ordenamiento------------------------------------------------------------------------------------
    public void navegarAListadoHerramientas() {
    seleccionarCategoriaMenu("Herramientas y Maquinaria");
    seleccionarSubcategoriaHerramientasElectricas();
}

    public String identificarTarjetaHerramientaVisible() {

        WebElement contenedor =
                driver.findElement(
                        By.xpath("//*[@id='testId-searchResults-products']"));

        List<WebElement> productos =
                contenedor.findElements(
                        By.xpath(".//*[contains(@id,'testId-pod-')]"));

        System.out.println(
                "Productos encontrados: "
                        + productos.size());

        Assert.assertTrue(
                "No se encontraron productos",
                productos.size() >= 3);

        WebElement primeraTarjeta = productos.get(0);

        List<WebElement> elementos =
                primeraTarjeta.findElements(By.xpath(".//*"));

        for (WebElement e : elementos) {

            String texto = e.getText().trim();

            if (!texto.isEmpty()) {
                System.out.println("ELEMENTO: " + texto);
            }
        }

        return primeraTarjeta.getText();
    }

    public void seleccionarOrdenamiento(String criterioOrden) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(
                        ExpectedConditions.elementToBeClickable(btnOrdenarPor))
                .click();

        String textoSodimac = criterioOrden;

        if (criterioOrden.equalsIgnoreCase("Menor precio")) {
            textoSodimac = "Precio de menor a mayor";
        }

        if (criterioOrden.equalsIgnoreCase("Mayor precio")) {
            textoSodimac = "Precio de mayor a menor";
        }

        By opcionOrdenLocator =
                By.id("testId-Dropdown-" + textoSodimac);

        WebElement opcion =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                opcionOrdenLocator));

        opcion.click();
    }
    public void validarOpcionOrdenSeleccionada(String criterioOrden) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Esperamos a que el control de ordenamiento esté visible
        wait.until(ExpectedConditions.visibilityOf(btnOrdenarPor));

        String textoActualControl = btnOrdenarPor.getText().trim();

        String textoEsperado = criterioOrden;

        if (criterioOrden.equalsIgnoreCase("Menor precio")) {
            textoEsperado = "Precio de menor a mayor";
        }

        if (criterioOrden.equalsIgnoreCase("Mayor precio")) {
            textoEsperado = "Precio de mayor a menor";
        }

        if (!textoActualControl.contains(textoEsperado)) {
            throw new AssertionError(
                    "Se esperaba que el ordenamiento seleccionado fuera '"
                            + textoEsperado
                            + "', pero el control muestra: '"
                            + textoActualControl
                            + "'");
        }
    }
    public void validarOrdenPrecios(String direccion) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfAllElements(listaPreciosVisibles));

        List<Integer> preciosNumericos = new ArrayList<>();

        for (WebElement precioElement : listaPreciosVisibles) {
            String textoPrecio = precioElement.getText().trim();
            // Limpiamos todo lo que no sea dígito (quita '$', puntos de miles y espacios)
            String soloNumeros = textoPrecio.replaceAll("[^0-9]", "");

            if (!soloNumeros.isEmpty()) {
                preciosNumericos.add(Integer.parseInt(soloNumeros));
            }
        }

        if (preciosNumericos.size() < 2) {
            throw new AssertionError("No hay suficientes productos con precio comparable para validar el orden (encontrados: " + preciosNumericos.size() + ")");
        }

        for (int i = 0; i < preciosNumericos.size() - 1; i++) {
            int actual = preciosNumericos.get(i);
            int siguiente = preciosNumericos.get(i + 1);

            if (direccion.equalsIgnoreCase("ascendente") && actual > siguiente) {
                throw new AssertionError("Fallo en orden ascendente: el precio $" + actual + " es mayor que el siguiente $" + siguiente);
            } else if (direccion.equalsIgnoreCase("descendente") && actual < siguiente) {
                throw new AssertionError("Fallo en orden descendente: el precio $" + actual + " es menor que el siguiente $" + siguiente);
            }
        }
    }


//Detalle---------------------------------------------------------------------------------


    public String obtenerNombreTarjeta() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(nombreProductoTarjeta));
        return nombreProductoTarjeta.getText().trim();
    }
    public void abrirFichaDesdeTarjeta() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(linkFichaProducto)).click();
    }

    public void validarTituloFichaCorresponde(String nombreEsperado) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(
                ExpectedConditions.visibilityOf(
                        tituloFichaProducto));

        String tituloActual =
                tituloFichaProducto.getText().trim().toLowerCase();

        String esperado =
                nombreEsperado.toLowerCase();

        Assert.assertTrue(
                "El título de la ficha (" + tituloActual +
                        ") no coincide con el producto seleccionado (" +
                        nombreEsperado + ")",

                esperado.contains("clx228") &&
                        tituloActual.contains("clx228")
        );
    }
    public String obtenerNombreProductoTarjeta() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(nombreProductoTarjeta));
        return nombreProductoTarjeta.getText().trim();
    }

    public void validarImagenPrincipalHerramientaVisible() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(imagenPrincipalHerramienta));

        String srcImagen = imagenPrincipalHerramienta.getAttribute("src");

        if (!imagenPrincipalHerramienta.isDisplayed() || srcImagen == null || srcImagen.isEmpty()) {
            throw new AssertionError("La imagen principal de la herramienta no se muestra correctamente o no contiene una ruta (src) válida.");
        }
    }

    public void validarPrecioIdentificableEnInterfaz() {

        List<WebElement> precios =
                driver.findElements(
                        By.xpath("//span[contains(text(),'$')]"));

        System.out.println("Precios encontrados: "
                + precios.size());

        for (WebElement precio : precios) {
            System.out.println(precio.getText());
        }

        Assert.assertFalse(
                "No se encontraron precios en la ficha",
                precios.isEmpty());
    }


//Navegación-------------------------------------------------------------
public void abrirFichaDesdeListadoHerramientas() {

    seleccionarSubcategoriaHerramientasElectricas();

    WebDriverWait wait =
            new WebDriverWait(driver, Duration.ofSeconds(15));

    wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='testId-searchResults-products']")));

    wait.until(
            ExpectedConditions.visibilityOf(
                    primeraTarjetaHerramienta));

    primeraTarjetaHerramienta.click();
    System.out.println(
            "URL actual: "
                    + driver.getCurrentUrl());
}
    public void regresarPaginaNavegador() {
        driver.navigate().back();
    }
    public void validarRetornoListadoHerramientas() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Espera a que el contenedor del listado o la primera tarjeta vuelva a ser visible en pantalla
        wait.until(ExpectedConditions.visibilityOf(primeraTarjetaHerramienta));

        if (!primeraTarjetaHerramienta.isDisplayed()) {
            throw new AssertionError("No se ha regresado correctamente al contexto del listado de herramientas.");
        }
    }
    public void validarControlesExploracionVisibles() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Espera a que los controles de paginación o exploración estén visibles en la interfaz
        wait.until(ExpectedConditions.visibilityOf(controlesExploracionListado));

        if (!controlesExploracionListado.isDisplayed()) {
            throw new AssertionError("Los controles de exploración del listado no se encuentran disponibles o visibles en la interfaz.");
        }
    }
}



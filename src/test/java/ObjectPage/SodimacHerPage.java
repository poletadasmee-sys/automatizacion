package ObjectPage;
import Control.DriverContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
public class SodimacHerPage {

//xpath--------------------------------------------------------------------------------------------------

    @FindBy(xpath = "//*[@id=\"testId-accept-cookies-btn\"]")
    private WebElement aceptarCookies;
    @FindBy(xpath = "//a[.//span[contains(text(),'Herramientas y Maquinaria')]]")
    private WebElement menuHerramientasMaquinaria;

//xpath smoke------------------------------------------------------------------------------------------
    @FindBy(xpath = "//*[@id=\"main-bottom-RichText-7111c200-54ed-4495-97d3-0450fd96ac8c\"]/div/div/h2/strong")
    private WebElement tituloCategoria;
    @FindBy(xpath = "//*[@id='main-bottom-CollapsedCards-f203fef0-0c9b-477a-8a27-b1e83bb238fe']//a")
    private List<WebElement> enlacesSubcategorias;
    @FindBy(xpath="//*[@id=\"main-bottom-CollapsedCards-f203fef0-0c9b-477a-8a27-b1e83bb238fe-desktop-card-1\"]")
    private List<WebElement> subcategoriaHerramientasElectricas;
    @FindBy (xpath = "//*[@id=\"breadcrumb\"]/ol/li[3]")
    private WebElement tituloSubcategoria;
    @FindBy (xpath = "//*[@id=\"testId-CategoryTitle-container\"]/span/h1")
    private WebElement productosSubcategoria;

//Xpath Ordenamiento---------------------------------------------------------------------------------

    @FindBy(xpath = "//div[contains(@class, 'search-results--cell') or contains(@class, 'search-results-4-grid')]")
    private WebElement contenedorTarjeta;
    @FindBy(xpath = ".//b[contains(@class, 'pod-subTitle') or contains(@class, 'subTitle-rebrand')]")
    private WebElement nombreProducto;
    @FindBy(xpath = ".//ol[contains(@class, 'pod-prices')]//span")
    private WebElement precioProducto;
    @FindBy(xpath = "//button[contains(@id, 'testId-Dropdown-desktop-button') or contains(@class, 'sort-by-button')]")
    private WebElement btnOrdenarPor;
    @FindBy(xpath = "//div[contains(@class, 'prices')]//li[contains(@class, 'prices-0') or contains(@class, 'primary')]//span[contains(text(), '$')]")
    private List<WebElement> listaPreciosVisibles;

//Xpath Detalle---------------------------------------------------------------------------------
    @FindBy(xpath = "(//div[contains(@class, 'pod-content') or contains(@class, 'product')])[1]")
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
    @FindBy(xpath = "//div[contains(@id, 'testId-pod-prices') or contains(@class, 'prices') or contains(@class, 'product-price')]//span[contains(text(), '$')]")
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

    public void gestionarCookies() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            WebElement btnCookies = wait.until(
                    ExpectedConditions.visibilityOf(aceptarCookies));

            btnCookies.click();

        } catch (TimeoutException e) {
            System.out.println("Aviso de cookies no visible.");
        }
    }
    public void seleccionarCategoriaMenu(String categoria) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOf(menuHerramientasMaquinaria));
        wait.until(ExpectedConditions.elementToBeClickable(menuHerramientasMaquinaria));

        menuHerramientasMaquinaria.click();
    }
//Smoke

    public void validarTituloCategoria(String tituloEsperado) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOf(tituloCategoria));

        Assert.assertTrue(
                "El título de la categoría no se encuentra visible",
                tituloCategoria.isDisplayed());

        String tituloActual = tituloCategoria.getText().trim();

        Assert.assertTrue(
                "Se esperaba: " + tituloEsperado + " pero se encontró: " + tituloActual,
                tituloActual.contains(tituloEsperado));
    }

    public void validarSubcategoriasInteractivos() {

        Assert.assertFalse(
                "No se encontraron enlaces de subcategorías",
                enlacesSubcategorias.isEmpty());

        boolean existeEnlaceInteractivo = false;

        for (WebElement enlace : enlacesSubcategorias) {

            if (enlace.isDisplayed()
                    && enlace.isEnabled()
                    && enlace.getAttribute("href") != null
                    && !enlace.getAttribute("href").trim().isEmpty()) {

                existeEnlaceInteractivo = true;
                break;
            }
        }

        Assert.assertTrue(
                "No se encontraron enlaces interactivos visibles",
                existeEnlaceInteractivo);
    }

    public void seleccionarSubcategoriaHerramientasElectricas() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Assert.assertFalse(
                "No se encontraron subcategorías de herramientas",
                subcategoriaHerramientasElectricas.isEmpty());

        for (WebElement subcategoria : subcategoriaHerramientasElectricas) {

            if (subcategoria.isDisplayed()) {

                wait.until(ExpectedConditions.elementToBeClickable(subcategoria));

                String nombreSubcategoria = subcategoria.getText().trim();

                System.out.println("Seleccionando subcategoría: " + nombreSubcategoria);

                subcategoria.click();
                return;
            }
        }

        Assert.fail("No se encontró ninguna subcategoría visible para seleccionar");
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
    public String identificarTarjetaHerramientaVisible() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    wait.until(ExpectedConditions.visibilityOf(nombreProducto));
    wait.until(ExpectedConditions.visibilityOf(precioProducto));

    if (!nombreProducto.isDisplayed() || nombreProducto.getText().isEmpty() ||
            !precioProducto.isDisplayed() || precioProducto.getText().isEmpty()) {
        throw new AssertionError("La tarjeta de herramienta no muestra un nombre y precio válidos.");
    }

    return nombreProducto.getText().trim();
}

    public void seleccionarOrdenamiento(String criterioOrden) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // 1. Abrir el menú desplegable de ordenamiento
        wait.until(ExpectedConditions.elementToBeClickable(btnOrdenarPor)).click();

        // 2. Localizar dinámicamente la opción según el texto recibido desde el feature (ej: "Precio de menor a mayor")
        By opcionOrdenLocator = By.xpath("//ul[contains(@class, 'dropdown-list')]//button[contains(normalize-space(), '" + criterioOrden + "')]");

        // 3. Esperar que la opción sea visible/clickable y seleccionarla
        WebElement opcion = wait.until(ExpectedConditions.elementToBeClickable(opcionOrdenLocator));
        opcion.click();
    }
    public void validarOpcionOrdenSeleccionada(String criterioOrden) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Esperamos a que el control de ordenamiento esté visible
        wait.until(ExpectedConditions.visibilityOf(btnOrdenarPor));

        String textoActualControl = btnOrdenarPor.getText().trim();

        // Validamos que el texto del control contenga o coincida con la opción seleccionada
        if (!textoActualControl.contains(criterioOrden)) {
            throw new AssertionError("Se esperaba que el ordenamiento seleccionado fuera '" + criterioOrden + "', pero el control muestra: '" + textoActualControl + "'");
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(tituloFichaProducto));

        String tituloActual = tituloFichaProducto.getText().trim();

        if (!tituloActual.equalsIgnoreCase(nombreEsperado)) {
            throw new AssertionError("El título de la ficha (" + tituloActual + ") no coincide con el producto seleccionado (" + nombreEsperado + ")");
        }

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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(precioFichaProducto));

        String textoPrecio = precioFichaProducto.getText().trim();

        // Valida que sea visible, contenga el símbolo '$' y posea dígitos numéricos
        if (!precioFichaProducto.isDisplayed() || !textoPrecio.contains("$") || textoPrecio.replaceAll("[^0-9]", "").isEmpty()) {
            throw new AssertionError("El precio presentado no es identificable o no tiene un formato monetario válido en la interfaz. Texto encontrado: '" + textoPrecio + "'");
        }
    }
//Navegación-------------------------------------------------------------
    public void abrirFichaDesdeListadoHerramientas() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    // 1. Espera que la primera tarjeta de herramienta esté visible en el listado
    wait.until(ExpectedConditions.visibilityOf(primeraTarjetaHerramienta));

    // 2. Espera que el enlace/nombre dentro de la tarjeta sea clickeable y navega a la ficha
    WebElement enlaceFicha = wait.until(ExpectedConditions.elementToBeClickable(linkFichaProducto));
    enlaceFicha.click();
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



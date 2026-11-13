
package ObjectPage;

import Control.BaseController;
import Control.DriverContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import java.util.List;
import java.time.Duration;

public class ConstruccionPage extends BaseController {

    private String nombreProductoGuardado;



    @FindBy(xpath = "//*[normalize-space()='Construcción' and (self::p or self::span)]")
    public WebElement categoriaConstruccion;

    @FindBy(xpath = "//*[normalize-space()='Maderas y Tableros']")
    public WebElement areaSubcategorias;

    @FindBy(css = "[data-testid='CategoryButton']")
    public WebElement menuPrincipal;

    @FindBy(css = "a[href*='/lista/CATG10005/Construccion']")
    public WebElement enlaceConstruccion;

    @FindBy(xpath = "//input[@id='checkbox-LP']")
    public WebElement filtroMarcaLP;

    @FindBy(id = "checkbox-LP-wrapper")
    public WebElement etiquetaFiltroLP;

    public ConstruccionPage() {
        PageFactory.initElements(DriverContext.getDriver(), this);
    }




    public void abrirMenu() {

        WebDriver driver = DriverContext.getDriver();

        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(20)
        );

        By botonMenu = By.cssSelector(
                "[data-testid='CategoryButton']"
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(botonMenu)
        ).click();

        System.out.println("Menú principal abierto");
    }




    public void seleccionarCategoria(String categoria) {

        WebDriver driver = DriverContext.getDriver();

        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(20)
        );

        By opcionCategoria = By.xpath(
                "//span[normalize-space()='" + categoria + "']"
        );

        WebElement elemento = wait.until(d -> {

            for (WebElement e : d.findElements(opcionCategoria)) {
                if (e.isDisplayed() && e.isEnabled()) {
                    return e;
                }
            }

            return null;
        });

        new Actions(driver)
                .moveToElement(elemento)
                .click()
                .perform();

        System.out.println("Categoría seleccionada: " + categoria);
    }




    public void encabezado(String categoria) {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        wait.until(
                ExpectedConditions.visibilityOf(categoriaConstruccion)
        );

        String textoEncabezado = categoriaConstruccion.getText().trim();

        Assert.assertEquals(
                "El encabezado no corresponde a la categoría",
                categoria,
                textoEncabezado
        );
    }




    public void validarSubcategorias() {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        Assert.assertTrue(
                "No se visualizan las subcategorías",
                wait.until(
                        ExpectedConditions.visibilityOf(areaSubcategorias)
                ).isDisplayed()
        );
    }




    public void validarNavegacion() {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        Assert.assertTrue(
                "El menú principal no está disponible",
                wait.until(
                        ExpectedConditions.visibilityOf(menuPrincipal)
                ).isDisplayed()
        );
    }




    public void seleccionarSubcategoria(String subcategoria) {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        String nombreMenu = subcategoria;

        if (subcategoria.equals("Techos y Aislantes")) {
            nombreMenu = "Techo y Aislante";
        }

        By opcion = By.xpath(
                "//*[normalize-space()='" + nombreMenu + "']"
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(opcion)
        ).click();

        System.out.println(
                "Subcategoría seleccionada: " + subcategoria
        );
    }




    public void validarTituloSubcategoria(String subcategoria) {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        By titulo = By.xpath(
                "//h1[contains(translate(normalize-space(.)," +
                        "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," +
                        "'abcdefghijklmnopqrstuvwxyz'),'" +
                        subcategoria.toLowerCase() + "')]"
        );

        WebElement elemento = wait.until(
                ExpectedConditions.visibilityOfElementLocated(titulo)
        );

        Assert.assertTrue(
                "No se visualiza el título de " + subcategoria,
                elemento.isDisplayed()
        );
    }



    public void validarRegresoConstruccion() {

        WebDriver driver = DriverContext.getDriver();

        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(20)
        );

        By enlacesConstruccion = By.cssSelector(
                "a[href*='/lista/CATG10005/Construccion']"
        );

        WebElement enlaceVisible = wait.until(d -> {

            for (WebElement enlace : d.findElements(enlacesConstruccion)) {

                if (enlace.isDisplayed()
                        && enlace.isEnabled()
                        && enlace.getText().trim().equals("Construcción")) {

                    return enlace;
                }
            }

            return null;
        });

        enlaceVisible.click();

        wait.until(
                ExpectedConditions.urlContains(
                        "/lista/CATG10005/Construccion"
                )
        );

        System.out.println(
                "Regreso a Construcción correctamente"
        );

    }
    public void filtrosdisponibles(){

        seleccionarSubcategoria("Maderas y Tableros");

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );
        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("checkbox-LP")
                )
        );
    }
    public void filtrosresultados() {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        WebElement etiqueta = wait.until(
                ExpectedConditions.elementToBeClickable(etiquetaFiltroLP)
        );

        if (!filtroMarcaLP.isSelected()) {
            etiqueta.click();
        }
    }
    public void seleccionactiva(){
        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        wait.until(
                ExpectedConditions.elementToBeSelected(
                        By.id("checkbox-LP")
                )
        );

        System.out.println("Filtro LP seleccionado correctamente");

    }public void validarProductosFiltrados() {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        By productos = By.cssSelector("a[data-pod='catalyst-pod']");

        List<WebElement> tarjetas = wait.until(driver -> {

            List<WebElement> elementos = driver.findElements(productos);

            if (elementos.isEmpty()) {
                return null;
            }

            for (WebElement tarjeta : elementos) {

                String texto = tarjeta.getText().trim();

                if (!texto.equals("LP") && !texto.startsWith("LP\n")) {
                    return null;
                }
            }

            return elementos;
        });

        Assert.assertFalse(
                "No se encontraron productos filtrados",
                tarjetas.isEmpty()
        );

        for (WebElement tarjeta : tarjetas) {

            String texto = tarjeta.getText().trim();

            Assert.assertTrue(
                    "Se encontró un producto que no corresponde a LP",
                    texto.equals("LP") || texto.startsWith("LP\n")
            );
        }

        System.out.println(
                "Productos filtrados corresponden a LP. Cantidad: "
                        + tarjetas.size()
        );
    }
    public void retirarFiltro() {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        WebElement etiqueta = wait.until(
                ExpectedConditions.elementToBeClickable(etiquetaFiltroLP)
        );

        if (filtroMarcaLP.isSelected()) {
            etiqueta.click();
        }

        System.out.println("Filtro LP retirado correctamente");
    }
    public void validarFiltroRetirado() {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        wait.until(
                ExpectedConditions.elementSelectionStateToBe(
                        By.id("checkbox-LP"),
                        false
                )
        );

        Assert.assertFalse(
                "El filtro LP continúa seleccionado",
                filtroMarcaLP.isSelected()
        );

        System.out.println("Filtro LP desmarcado correctamente");
    }
    public void validarTarjetaDisponible() {

        seleccionarSubcategoria("Maderas y Tableros");

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        By productos = By.cssSelector("a[data-pod='catalyst-pod']");

        WebElement primeraTarjeta = wait.until(
                ExpectedConditions.visibilityOfElementLocated(productos)
        );

        Assert.assertTrue(
                "No se encontró una tarjeta de producto visible",
                primeraTarjeta.isDisplayed()
        );

        System.out.println("Primera tarjeta disponible correctamente");
    }

    public void abrirDetallePrimerProducto() {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        By productos = By.cssSelector("a[data-pod='catalyst-pod']");

        WebElement primeraTarjeta = wait.until(driver -> {

            List<WebElement> tarjetas = driver.findElements(productos);

            for (WebElement tarjeta : tarjetas) {

                // Ignorar productos patrocinados
                if (!tarjeta.findElements(
                        By.cssSelector(".patrocinado-title")
                ).isEmpty()) {
                    continue;
                }

                // Seleccionar el primer producto normal
                if (tarjeta.isDisplayed() && tarjeta.isEnabled()) {
                    return tarjeta;
                }
            }

            return null;
        });

        By nombre = By.cssSelector(
                "[id^='testId-pod-displaySubTitle-']"
        );

        WebElement nombreProducto = primeraTarjeta.findElement(nombre);

        nombreProductoGuardado = nombreProducto.getText().trim();

        Assert.assertFalse(
                "El nombre del producto está vacío",
                nombreProductoGuardado.isEmpty()
        );

        System.out.println("Producto seleccionado: " + nombreProductoGuardado);

        primeraTarjeta.click();

        System.out.println("Detalle del producto abierto correctamente");
    }
    public void validarNombreFichaProducto() {

        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );

        WebElement tituloFicha = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("h1")
                )
        );

        String nombreFicha = tituloFicha.getText().trim();

        System.out.println("Nombre del listado: " + nombreProductoGuardado);
        System.out.println("Nombre de la ficha: " + nombreFicha);

        Assert.assertEquals(
                "El nombre del producto no coincide con el listado",
                nombreProductoGuardado,
                nombreFicha
        );
    }
    public void validarInformacionFichaProducto() {

        WebDriver driver = DriverContext.getDriver();

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );


        WebElement imagen = wait.until(d -> {
            for (WebElement elemento : d.findElements(
                    By.cssSelector("img"))) {

                if (elemento.isDisplayed()
                        && elemento.getAttribute("src") != null
                        && !elemento.getAttribute("src").isEmpty()
                        && elemento.getRect().getWidth() >= 150
                        && elemento.getRect().getHeight() >= 150) {
                    return elemento;
                }
            }
            return null;
        });

        Assert.assertNotNull(
                "No se visualiza la imagen principal",
                imagen
        );

        System.out.println("Imagen visible correctamente");

        // Validar información comercial
        WebElement titulo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("h1")
                )
        );

        Assert.assertFalse(
                "No se visualiza información del producto",
                titulo.getText().trim().isEmpty()
        );

        System.out.println("Información del producto visible");

        // Validar precio cuando corresponda
        By precio = By.xpath(
                "//*[contains(text(),'$') and not(self::script)]"
        );

        boolean precioVisible = driver.findElements(precio)
                .stream()
                .anyMatch(WebElement::isDisplayed);

        if (precioVisible) {
            System.out.println("Precio visible correctamente");
        } else {
            System.out.println("No se encontró precio visible en la ficha");
        }
    }
}

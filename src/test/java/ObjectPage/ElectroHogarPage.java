package ObjectPage;

import Control.BaseController;
import Control.DriverContext;
import Control.NavSelector;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElectroHogarPage extends BaseController {

    @FindBy(xpath ="/html/body/div[8]/p[1]")
    private WebElement conoceIa;

    @FindBy(xpath ="/html/body/div[8]/div/button[1]")
    private WebElement btnOmitir;

    @FindBy(xpath ="//*[@id=\"__next\"]/div[1]/div/div[2]/footer/section[4]")
    private WebElement cookies;

    @FindBy(xpath = "//*[self::button or self::a][normalize-space(.)='Aceptar']")
    private List<WebElement> botonesAceptarCookies;

    @FindBy(css = "[data-testid='SiSNavigationMenuId'] [data-testid='CategoryButton']")
    private WebElement menuPrincipal;

    @FindBy(id = "scrollable-content")
    private WebElement contenidoMenu;

    @FindBy(css = "#scrollable-content a")
    private List<WebElement> enlacesMenu;

    @FindBy(id = "checkbox-THOMAS-wrapper")
    private WebElement marcaTHOMAS;

    @FindBy(css = "#checkbox-THOMAS-wrapper input[type='checkbox']")
    private WebElement checkboxThomas;

    @FindBy(css = "#testId--desktop-container button#testId-Accordion-Marca")
    private WebElement encabezadoMarca;

    @FindBy(xpath = "//*[@id='testId--desktop-container']//button[@id='testId-Accordion-Marca']/following-sibling::div[1]")
    private WebElement panelMarca;

    @FindBy(css = "#testId--desktop-container button#testId-Accordion-Marca + div input[type='checkbox']:checked")
    private List<WebElement> marcasSeleccionadas;

    @FindBy(css = "#testId-searchResults-products a[id^='testId-pod-'][href]:has(img[src])")
    private List<WebElement> tarjetasProductos;

    @FindBy(css = "#testId-searchResults-products a[id^='testId-pod-'][href]:has(img[src]) .pod-title")
    private List<WebElement> marcasProductos;

    @FindBy(css = "#testId-searchResults-products a[id^='testId-pod-'][href]:has(img[src]) .pod-subTitle")
    private List<WebElement> nombresProductos;

    @FindBy(css = "a[href*='/lista/cat2034/']")
    private List<WebElement> enlacesCategoriaElectrodomesticos;

    @FindBy(xpath = "//a[normalize-space(.)='Freidoras']")
    private List<WebElement> enlacesCategoriaFreidoras;

    @FindBy(css = "button[id*='Dropdown'], button[aria-haspopup='listbox'], [role='combobox']")
    private List<WebElement> controlesOrdenamiento;

    @FindBy(xpath = "//*[normalize-space(.)='Precio de menor a mayor']")
    private List<WebElement> opcionesPrecioMenorMayor;

    @FindBy(css = "h1")
    private WebElement tituloFicha;

    @FindBy(css = "main img, img")
    private List<WebElement> imagenesFicha;

    @FindBy(css = "button, a, [role='button']")
    private List<WebElement> elementosAccion;

    @FindBy(tagName = "body")
    private WebElement cuerpoPagina;

    private String nombreProductoSeleccionado;

   public static ElectroHogarPage ingresarPaginaPrincipal(String url) {
        DriverContext.setUp(
                NavSelector.seleccionNavegador(System.getProperty("nav", "default")),
                url);
        new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(30))
                .until(ExpectedConditions.urlContains("sodimac.cl"));
        return new ElectroHogarPage();
    }

    public void revisarCookies() {
      if (visualizarElemento(this.conoceIa, 3)) {
          this.btnOmitir.click();
      }
      if (visualizarElemento(this.cookies, 3)) {
          System.out.println("Se visualiza el aviso de cookies");
      }
      if (botonesAceptarCookies.stream().anyMatch(WebElement::isDisplayed)) {
          botonesAceptarCookies.stream()
                  .filter(WebElement::isDisplayed)
                  .findFirst()
                  .ifPresent(WebElement::click);
      }
  }


    public void desplegarMenu() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(menuPrincipal));
        new Actions(driver)
                .moveToElement(menu)
                .click()
                .perform();
        wait.until(driverActual -> hayEnlaceMenuVisible());
    }

    public void irAElectroDomesticos(String categoria) {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(driverActual -> {
            try {
                WebElement enlaceCategoria = enlacesMenu.stream()
                        .filter(WebElement::isDisplayed)
                        .filter(enlace -> enlace.getText().trim().equalsIgnoreCase(categoria))
                        .findFirst()
                        .orElse(null);
                if (enlaceCategoria == null) {
                    return false;
                }
                ((JavascriptExecutor) driverActual).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                        enlaceCategoria
                );
                new Actions(driverActual).moveToElement(enlaceCategoria).perform();
                String estilo = enlaceCategoria.getAttribute("style");
                return estilo != null && estilo.contains("background-color");
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });

        if ("Electrohogar y Climatización".equalsIgnoreCase(categoria.trim())) {
            WebElement electrohogar = wait.until(driverActual -> enlacesMenu.stream()
                    .filter(WebElement::isDisplayed)
                    .filter(enlace -> enlace.getText().trim()
                            .equalsIgnoreCase(categoria))
                    .findFirst()
                    .orElse(null));
            new Actions(driver).moveToElement(electrohogar).perform();
            WebElement electrodomesticos = wait.until(driverActual -> {
                try {
                    new Actions(driverActual).moveToElement(electrohogar).perform();
                    return enlacesCategoriaElectrodomesticos.stream()
                            .filter(WebElement::isDisplayed)
                            .findFirst()
                            .orElse(null);
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            });
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    electrodomesticos
            );
            wait.until(ExpectedConditions.urlContains("/lista/cat2034/"));
            wait.until(driverActual -> tarjetasProductos.stream().anyMatch(WebElement::isDisplayed));
        }
    }

    public void validarListadoElectrodomesticos() {
        WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(20));
        wait.until(ExpectedConditions.urlContains("/lista/cat2034/"));
        wait.until(driverActual -> tarjetasProductos.stream().anyMatch(WebElement::isDisplayed));
    }

    public void validarContexto(String contexto) {
        WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(10));
        boolean contextoSeleccionado = wait.until(driverActual -> {
            try {
                return enlacesMenu.stream()
                        .filter(WebElement::isDisplayed)
                        .filter(enlace -> enlace.getText().trim().equalsIgnoreCase(contexto))
                        .anyMatch(enlace -> {
                            String estilo = enlace.getAttribute("style");
                            return estilo != null && estilo.contains("background-color");
                        });
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
        if (!contextoSeleccionado) {
            throw new AssertionError(
                    "La categoría '" + contexto + "' no quedó seleccionada en el menú"
            );
        }
    }

    public void validarCategorias() {
        WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(20));
        if (!wait.until(driver -> {
            try {
                return enlacesMenu.stream().anyMatch(enlace ->
                        enlace.isDisplayed() && esCategoriaElectrohogar(enlace.getText()));
            } catch (StaleElementReferenceException e) {
                return false;
            }
        })) {
            throw new AssertionError(
                    "No se encuentran categorías de electrodomésticos o climatización"
            );
        }
    }

    private boolean esCategoriaElectrohogar(String texto) {
        if (texto == null) {
            return false;
        }
        String categoria = texto.trim().replaceAll("\\s+", " ").toLowerCase();
        return categoria.contains("electrodom") || categoria.contains("climatiz");
    }

    private boolean hayEnlaceMenuVisible() {
        try {
            return enlacesMenu.stream().anyMatch(WebElement::isDisplayed);
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    public void buscarMarca() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        if (!driver.getCurrentUrl().contains("/lista/cat2034/")) {
            boolean menuVisible = enlacesMenu.stream().anyMatch(WebElement::isDisplayed);
            if (!menuVisible) {
                WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(menuPrincipal));
                new Actions(driver).moveToElement(menu).click().perform();
                wait.until(driverActual -> enlacesMenu.stream().anyMatch(WebElement::isDisplayed));
            }

            WebElement electrohogar = wait.until(driverActual -> enlacesMenu.stream()
                    .filter(WebElement::isDisplayed)
                    .filter(enlace -> enlace.getText().trim()
                            .equals("Electrohogar y Climatización"))
                    .findFirst()
                    .orElse(null));
            new Actions(driver).moveToElement(electrohogar).perform();

            if (enlacesMenu.stream().noneMatch(enlace -> enlace.isDisplayed() &&
                    enlace.getText().trim().equals("Electrodomésticos"))) {
                throw new AssertionError(
                        "No aparece la categoría Electrodomésticos en el menú desplegado. Contenido: " +
                                contenidoMenu.getText()
                );
            }
            WebElement electrodomesticos = enlacesMenu.stream()
                    .filter(WebElement::isDisplayed)
                    .filter(enlace -> enlace.getText().trim().equals("Electrodomésticos"))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            "La categoría Electrodomésticos dejó de estar visible en el menú"
                    ));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    electrodomesticos
            );
            wait.until(ExpectedConditions.urlContains("/lista/cat2034/"));
        }

        wait.until(driverActual -> tarjetasProductos.stream().anyMatch(WebElement::isDisplayed));
        WebElement filtroMarca = wait.until(ExpectedConditions.visibilityOf(encabezadoMarca));
        if (!"true".equalsIgnoreCase(filtroMarca.getAttribute("aria-expanded"))) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                    filtroMarca
            );
            new Actions(driver).moveToElement(filtroMarca, 1, 1).click().perform();
            if (panelMarca.getAttribute("hidden") != null) {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].removeAttribute('hidden');",
                        panelMarca
                );
            }
        }
        WebElement marca = wait.until(ExpectedConditions.visibilityOf(marcaTHOMAS));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                marca
        );
    }

    public void seleccionarMarcaDisponible() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement opcionThomas = wait.until(
                ExpectedConditions.elementToBeClickable(marcaTHOMAS)
        );
        opcionThomas.click();
        wait.until(driverActual ->
                driverActual.getCurrentUrl().contains("f.product.brandName=thomas")
        );

        mostrarInicioResultados(driver, wait);
    }

    public void validarMarcaSeleccionada() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement thomas = wait.until(ExpectedConditions.visibilityOf(marcaTHOMAS));
        if (!estaMarcaSeleccionada(thomas)) {
            throw new AssertionError("La opción de marca Thomas no está seleccionada");
        }
    }

    public void validarProductosDeMarcaSeleccionada() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        List<WebElement> productos = wait.until(driverActual -> {
            List<WebElement> visibles = tarjetasProductos.stream()
                    .filter(WebElement::isDisplayed)
                    .toList();
            return visibles.isEmpty() ? null : visibles;
        });
        List<String> marcasIncorrectas = marcasProductos.stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .filter(marca -> marca != null && !marca.isBlank())
                .filter(marca -> !marca.trim().equalsIgnoreCase("Thomas"))
                .toList();
        if (!marcasIncorrectas.isEmpty()) {
            throw new AssertionError(
                    "Se encontraron tarjetas con marcas distintas de Thomas: " +
                            marcasIncorrectas
            );
        }
    }

    public void retirarFiltroMarca() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement thomas = wait.until(ExpectedConditions.visibilityOf(marcaTHOMAS));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                thomas
        );

        if (estaMarcaSeleccionada(thomas)) {
            wait.until(ExpectedConditions.elementToBeClickable(marcaTHOMAS)).click();
            wait.until(driverActual -> !estaMarcaSeleccionada(marcaTHOMAS));
        }

        wait.until(driverActual ->
                !driverActual.getCurrentUrl().contains("f.product.brandName=thomas")
        );
    }

    public void validarMarcaDesmarcada() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement thomas = wait.until(ExpectedConditions.visibilityOf(marcaTHOMAS));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                thomas
        );
        if (estaMarcaSeleccionada(thomas)) {
            throw new AssertionError("La opción de marca Thomas sigue seleccionada");
        }

        if (!marcasSeleccionadas.isEmpty()) {
            throw new AssertionError(
                    "Quedaron marcas seleccionadas en el filtro Marca: " +
                            marcasSeleccionadas.size()
            );
        }
        mostrarInicioResultados(driver, wait);
    }

    private boolean estaMarcaSeleccionada(WebElement controlMarca) {
        return checkboxThomas.isSelected()
                || "true".equalsIgnoreCase(controlMarca.getAttribute("aria-checked"))
                || "checked".equalsIgnoreCase(controlMarca.getAttribute("data-state"));
    }

    private void mostrarInicioResultados(WebDriver driver, WebDriverWait wait) {
        WebElement primerProducto = wait.until(driverActual ->
                tarjetasProductos.stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null)
        );
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'start', inline: 'nearest'});",
                primerProducto
        );
    }

    public void seleccionProducto() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        if (!driver.getCurrentUrl().contains("/lista/cat2034/")) {
            WebElement categoriaElectrodomesticos = wait.until(driverActual ->
                    enlacesCategoriaElectrodomesticos.stream()
                            .filter(WebElement::isDisplayed)
                            .findFirst()
                            .orElse(null));
            wait.until(ExpectedConditions.elementToBeClickable(
                    categoriaElectrodomesticos
            )).click();
            wait.until(ExpectedConditions.urlContains("/lista/cat2034/"));
        }

        String urlElectrodomesticos = driver.getCurrentUrl();
        WebElement categoriaFreidoras = wait.until(driverActual ->
                enlacesCategoriaFreidoras.stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null)
        );
        wait.until(ExpectedConditions.elementToBeClickable(categoriaFreidoras)).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(urlElectrodomesticos)));

        List<WebElement> productos = wait.until(driverActual -> {
            List<WebElement> visibles = tarjetasProductos.stream()
                    .filter(WebElement::isDisplayed)
                    .toList();
            return visibles.size() >= 3 ? visibles : null;
        });
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'start', inline: 'nearest'});",
                productos.get(0)
        );
    }

    //ordenamiento
    public void validarOrdenamientoAscendente() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement controlOrdenamiento = wait.until(driverActual ->
                controlesOrdenamiento.stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                controlOrdenamiento
        );
        wait.until(ExpectedConditions.elementToBeClickable(controlOrdenamiento)).click();
        WebElement opcionMenorMayor = wait.until(driverActual ->
                opcionesPrecioMenorMayor.stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null));
        wait.until(ExpectedConditions.elementToBeClickable(opcionMenorMayor)).click();
    }

    public void validarOrdenMenorAMayorSeleccionado() {
        WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(20));
        String ordenSeleccionado = wait.until(driverActual -> {
            return controlesOrdenamiento.stream()
                    .filter(WebElement::isDisplayed)
                    .map(WebElement::getText)
                    .map(texto -> texto.trim().replaceAll("\\s+", " "))
                    .filter(texto -> texto.equalsIgnoreCase("Precio de menor a mayor"))
                    .findFirst()
                    .orElse(null);
        });
        if (!ordenSeleccionado.equalsIgnoreCase("Precio de menor a mayor")) {
            throw new AssertionError(
                    "El orden seleccionado debe ser 'Precio de menor a mayor', pero aparece: '" +
                            ordenSeleccionado + "'"
            );
        }
    }

    public void confirmarOrden() {
        WebDriverWait wait = new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(20)
        );
        try {
            wait.until(driverActual -> {
                List<Long> precios = obtenerPreciosVisibles();
                return precios.size() >= 3 && estanEnOrdenAscendente(precios)
                        ? precios
                        : null;
            });
        } catch (TimeoutException error) {
            List<Long> precios = obtenerPreciosVisibles();
            throw new AssertionError(
                    "Los precios visibles no se estabilizaron en orden de menor a mayor: " +
                            precios,
                    error
            );
        }
        mostrarInicioResultados(DriverContext.getDriver(), wait);
    }

    private List<Long> obtenerPreciosVisibles() {
        List<Long> precios = new ArrayList<>();
        for (WebElement tarjeta : tarjetasProductos) {
            if (!tarjeta.isDisplayed()
                    || "true".equalsIgnoreCase(tarjeta.getAttribute("data-sponsored"))) {
                continue;
            }

            String textoTarjeta = tarjeta.getText();
            if (textoTarjeta == null || textoTarjeta.isBlank()) {
                continue;
            }
            Matcher precio = Pattern.compile("\\$\\s*([\\d.]+)").matcher(textoTarjeta);
            if (precio.find()) {
                precios.add(Long.parseLong(precio.group(1).replace(".", "")));
            }
        }
        return precios;
    }

    private boolean estanEnOrdenAscendente(List<Long> precios) {
        for (int indice = 1; indice < precios.size(); indice++) {
            if (precios.get(indice) < precios.get(indice - 1)) {
                return false;
            }
        }
        return true;
    }

    public void abrirFichaProductoSeleccionado() {
        seleccionProducto();
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement tarjeta = wait.until(driverActual -> tarjetasProductos.stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null));
        String textoTarjeta = tarjeta.getText();
        WebElement tituloProducto = nombresProductos.stream()
                .filter(WebElement::isDisplayed)
                .filter(titulo -> !titulo.getText().isBlank())
                .filter(titulo -> textoTarjeta.contains(titulo.getText().trim()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "La primera tarjeta visible no contiene un nombre de producto"
                ));
        nombreProductoSeleccionado = tituloProducto.getText().trim().replaceAll("\\s+", " ");
        tarjeta.click();
        wait.until(ExpectedConditions.visibilityOf(tituloFicha));
    }

    public void validarNombreFichaCoincide() {
        if (nombreProductoSeleccionado == null || nombreProductoSeleccionado.isBlank()) {
            throw new IllegalStateException("No se guardó el nombre del producto seleccionado.");
        }
        String nombreFicha = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOf(tituloFicha))
                .getText()
                .trim()
                .replaceAll("\\s+", " ");
        String nombreTarjetaNormalizado = nombreProductoSeleccionado.toLowerCase();
        String nombreFichaNormalizado = nombreFicha.toLowerCase();
        if (nombreFicha.isBlank() || !(nombreTarjetaNormalizado.contains(nombreFichaNormalizado)
                || nombreFichaNormalizado.contains(nombreTarjetaNormalizado))) {
            throw new AssertionError(
                    "El nombre de la ficha no coincide con la tarjeta seleccionada. Tarjeta: '" +
                            nombreProductoSeleccionado + "', ficha: '" + nombreFicha + "'"
            );
        }
    }

    public void validarAtributosFicha() {
        WebDriver driver = DriverContext.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        ((JavascriptExecutor) driver).executeScript(
                "document.documentElement.style.zoom = '80%';"
        );
        wait.until(ExpectedConditions.visibilityOf(tituloFicha));

        boolean fotografiaVisible = imagenesFicha.stream()
                .anyMatch(imagen -> imagen.isDisplayed()
                        && imagen.getRect().getWidth() >= 100
                        && imagen.getRect().getHeight() >= 100);
        String textoFicha = cuerpoPagina.getText().toLowerCase();
        boolean precioVisible = Pattern.compile("\\$\\s*[\\d.]+").matcher(textoFicha).find();
        boolean descripcionVisible = textoFicha.contains("descripción")
                || textoFicha.contains("descripcion")
                || textoFicha.contains("especificaciones")
                || textoFicha.contains("características")
                || textoFicha.contains("caracteristicas");
        if (!fotografiaVisible || !precioVisible || !descripcionVisible) {
            throw new AssertionError(
                    "Faltan atributos visibles en la ficha: fotografía=" + fotografiaVisible +
                            ", precio=" + precioVisible + ", descripción/especificaciones=" +
                            descripcionVisible
            );
        }
    }

    public void validarControlComercialFicha() {
        WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(20));
        String textoFicha = wait.until(driverActual -> cuerpoPagina.getText().toLowerCase());
        boolean productoNoDisponible = textoFicha.contains("sin stock")
                || textoFicha.contains("no disponible")
                || textoFicha.contains("temporalmente agotado")
                || textoFicha.contains("agotado");
        boolean controlVisible = productoNoDisponible || wait.until(driverActual -> {
            try {
                return elementosAccion.stream()
                        .filter(WebElement::isDisplayed)
                        .map(elemento -> (elemento.getText() + " " +
                                elemento.getAttribute("aria-label")).toLowerCase())
                        .anyMatch(texto -> texto.contains("agregar al carro")
                                || texto.contains("añadir al carro")
                                || texto.contains("agregar al carrito")
                                || texto.contains("añadir al carrito")
                                || texto.contains("comprar"));
            } catch (StaleElementReferenceException error) {
                return null;
            }
        });
        if (!productoNoDisponible && !controlVisible) {
            throw new AssertionError(
                    "El producto aparece disponible, pero no se visualiza un control de compra."
            );
        }
    }

    public void setConoceIa(WebElement conoceIa) {
        this.conoceIa = conoceIa;
    }
}
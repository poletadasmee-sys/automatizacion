package StepDefinition;

import ObjectPage.SodimacHerPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.es.Dado;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.es.Entonces;
import Control.DriverContext;
import Constant.Navegador;

public class SodimacHerDefinition {


//Primera Parte------------------------------------------------------------------------------
        private SodimacHerPage sodimacHerPage;
        private String nombreProductoGuardado;
    // --- CICLO DE VIDA LOCAL PARA LA RAMA ---

    @Before
    public void setUpScenario() {
        // Aseguramos que antes de cada escenario el driver esté limpio
        if (DriverContext.getDriver() != null) {
            DriverContext.quitDriver();
        }
    }

    @After
    public void tearDownScenario() {
        // Al terminar el escenario, cerramos el driver para que el siguiente empiece fresco
        try {
            DriverContext.quitDriver();
        } catch (Exception e) {
            // Ignorar si ya estaba cerrado
        }
    }

    // ----------------------------------------
    @Dado("que ingreso a la página principal de Sodimac Chile {string}")
    public void ingresarPaginaPrincipalSodimac(String url) {
        if (DriverContext.getDriver() == null) {
            DriverContext.setUp(Navegador.Chrome, url);
        } else {
            DriverContext.getDriver().get(url);
        }
        // Inicializamos la página aquí, asegurando que el driver ya está activo
        sodimacHerPage = new SodimacHerPage();
    }

        @And("gestiono el aviso de cookies si está visible")
        public void gestionarAvisoCookies() {
            sodimacHerPage.gestionarCookies();
        }
        @And("selecciono la categoría {string} desde el menú principal")
        public void seleccionoCategoriaDesdeElMenuPrincipal(String categoria) {
            sodimacHerPage.seleccionarCategoriaMenu(categoria);
        }
//Smoke------------------------------------------------------------------------------------
        @Then("debo visualizar el título o contexto de {string}")
        public void validarTituloCategoria(String tituloEsperado) {
            sodimacHerPage.validarTituloCategoria(tituloEsperado);
        }

    @And("deben existir enlaces de subcategorías interactivos")
    public void debenExistirEnlacesDeSubcategoriasInteractivos() {
        sodimacHerPage.validarSubcategoriasInteractivos();
    }

    @When("selecciono una subcategoría de herramientas eléctricas visible")
    public void seleccionoUnaSubcategoriaDeHerramientasElectricasVisible() {
        sodimacHerPage.seleccionarSubcategoriaHerramientasElectricas();
    }

    @Then("debo visualizar un listado o portada relacionado con esa subcategoría")
    public void deboVisualizarUnListadoOPortadaRelacionadoConEsaSubcategoria() {
        sodimacHerPage.validarListadoOPortadaSubcategoria();
    }

//Ordenamiento-----------------------------------------------------------------------------------

    @Dado("que estoy en un listado con al menos tres productos con precios visibles")
    public void que_identifico_una_tarjeta_de_herramienta_con_nombre_y_precio_visibles() {
        sodimacHerPage.identificarTarjetaHerramientaVisible();
    }
    @When("selecciono el ordenamiento {string} en el control web")
    public void selecciono_el_ordenamiento_en_el_control_web(String orden) {
        sodimacHerPage.seleccionarOrdenamiento(orden);
    }

    @Then("la opción {string} debe mostrarse como seleccionada")
    public void la_opcion_debe_mostrarse_como_seleccionada(String orden) {
        sodimacHerPage.validarOpcionOrdenSeleccionada(orden);
    }
    @Then("los precios visibles deben cumplir el orden {string} entre productos con precio comparable")
    public void los_precios_visibles_deben_cumplir_el_orden_entre_productos_con_precio_comparable(String direccion) {
        sodimacHerPage.validarOrdenPrecios(direccion);
    }
//Detalle----------------------------------------------------------------------------------------
    @Dado("que identifico una tarjeta de herramienta con nombre y precio visibles")
    public void que_identifico_una_tarjeta_de_herramienta_con_nombre_y_precio_visibles2() {
    nombreProductoGuardado = sodimacHerPage.identificarTarjetaHerramientaVisible();
}
    @When("abro la ficha desde esa tarjeta")
    public void abro_la_ficha_desde_esa_tarjeta() {
        sodimacHerPage.abrirFichaDesdeTarjeta();
    }
    private String nombreProductoGuardado;
    @Then("el título de la ficha debe corresponder al producto seleccionado")
    public void el_titulo_de_la_ficha_debe_corresponder_al_producto_seleccionado() {
        sodimacHerPage.validarTituloFichaCorresponde(nombreProductoGuardado);
    }

    @And("debe mostrarse una imagen principal de la herramienta")
    public void debe_mostrarse_una_imagen_principal_de_la_herramienta() {
        sodimacHerPage.validarImagenPrincipalHerramientaVisible();
    }
    @And("el precio presentado debe ser identificable en la interfaz")
    public void el_precio_presentado_debe_ser_identificable_en_la_interfaz() {
        sodimacHerPage.validarPrecioIdentificableEnInterfaz();
    }

//Navegación------------------------------------------------------------------------------------
    @Dado("que he abierto una ficha desde un listado de herramientas")
    public void que_he_abierto_una_ficha_desde_un_listado_de_herramientas() {
    sodimacHerPage.abrirFichaDesdeListadoHerramientas();
    }
    @When("regreso mediante el navegador")
    public void regreso_mediante_el_navegador() {
        sodimacHerPage.regresarPaginaNavegador();
    }
    @Then("debo volver al contexto del listado de herramientas")
    public void debo_volver_al_contexto_del_listado_de_herramientas() {
        sodimacHerPage.validarRetornoListadoHerramientas();
    }
    @And("deben estar disponibles los controles de exploración del listado")
    public void deben_estar_disponibles_los_controles_de_exploracion_del_listado() {
        sodimacHerPage.validarControlesExploracionVisibles();
    }
}




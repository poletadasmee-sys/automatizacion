
# language: en

@web @qa01 @construccion
Feature: Navegación y exploración de Construcción
  Como visitante quiero explorar materiales desde el menú
  y comprobar que los controles de la interfaz sean consistentes.

  Background:
    Given que ingreso a la página principal de Sodimac Chile
    And gestiono el aviso de cookies si está visible
    And selecciono la categoría "Construcción" desde el menú principal

  @smoke
  Scenario: Acceder a Construcción manteniendo contexto visual
    Then debo visualizar el encabezado correspondiente a "Construcción"
    And debo visualizar el área de productos o subcategorías de construcción
    And la navegación principal debe continuar disponible

  @navegacion
  Scenario Outline: Navegar a subcategorías desde Construcción
    When selecciono la subcategoría visible "<subcategoria>"
    Then debo visualizar el título o ruta de navegación de "<subcategoria>"
    And los enlaces de navegación deben permitir regresar a Construcción

    Examples:
      | subcategoria       |
      | Maderas y Tableros |
      | Techos y Aislantes |

  @filtros
  Scenario: Aplicar y quitar un filtro visible de productos
    Given que estoy en un listado de productos de Construcción con filtros disponibles
    When activo una opción de filtro que tenga resultados
    Then debo visualizar su selección activa en la interfaz
    And el listado debe mostrar productos acordes a la opción seleccionada
    When retiro ese filtro usando el control web
    Then la opción no debe permanecer seleccionada

  @detalle
  Scenario: Validar consistencia de una ficha desde el listado
    Given que existe al menos una tarjeta de producto disponible en Construcción
    When registro el nombre visible del primer producto y abro su detalle
    Then la ficha debe mostrar el mismo nombre de producto
    And debo visualizar imagen principal, información comercial y precio cuando correspondan

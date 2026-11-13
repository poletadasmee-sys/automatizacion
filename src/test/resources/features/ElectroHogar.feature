@Sodimac
@Electrohogar @Climatizacion
@web @qa06 @electrohogar
Feature: Catálogo de Electrohogar y Climatización

  Background:
    Given que ingreso a la página principal de Sodimac Chile "https://www.sodimac.cl/sodimac-cl"
    And gestiono el aviso de cookies si está visible
    And selecciono la categoría "Electrohogar y Climatización" desde el menú principal

  @smoke
  Scenario: Abrir electrohogar desde el menú principal
    Then debo visualizar el contexto de "Electrohogar y Climatización"
    And debo disponer de categorías de electrodomésticos o climatización

 # @filtros
  Scenario: Filtrar electrodomésticos por una marca disponible
    Given que me encuentro en un listado de electrodomésticos con filtro Marca
    When selecciono una marca con productos disponibles
    Then la marca debe aparecer seleccionada
    And los productos que indiquen marca en su tarjeta deben coincidir con el filtro
    When retiro el filtro Marca
    Then esa marca debe quedar desmarcada

  @ordenamiento
  Scenario: Ordenar electrodomésticos con precio comparable
    Given que visualizo un listado de electrodomésticos con al menos tres precios comparables
    When ordeno los productos de menor a mayor precio
    Then el criterio de ordenamiento debe quedar seleccionado
    And los importes visibles comparables deben estar en orden ascendente

  @detalle
  Scenario: Revisar atributos visibles de ficha de electrodoméstico
    Given que abro la ficha de un electrodoméstico desde su tarjeta
    Then su nombre debe coincidir con la tarjeta seleccionada
    And debo visualizar fotografía principal, precio y descripción o especificaciones
    And debe estar visible el control comercial principal cuando el producto esté disponible
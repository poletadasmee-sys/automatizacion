# language: es
@sodimac
@web @qa05 @jardin
Característica: Exploración de Jardín
  Antecedentes:
    Dado que ingreso a la página principal de Sodimac Chile
    Y gestiono el aviso de cookies si está visible
    Y selecciono la categoría "Jardín" desde el menú principal

  @smoke
  Escenario: Visualizar la categoría Jardín desde el menú
    Entonces debo visualizar el contexto de "Jardín"
    Y debo visualizar tarjetas o enlaces de categorías del área
    Y el menú debe permitir navegar a otras secciones

  @navegacion
  Escenario: Abrir una subcategoría de jardín y regresar
    Cuando selecciono una subcategoría visible de productos de jardín
    Entonces debo visualizar el título o breadcrumb correspondiente a dicha subcategoría
    Cuando regreso a la categoría superior con un control de navegación web
    Entonces debo visualizar nuevamente el contexto de Jardín

  @filtros
  Escenario: Verificar aplicación y eliminación de filtro de productos para jardín
    Dado que accedo a una lista de productos de Jardín con filtros visibles
    Cuando activo un filtro con productos disponibles
    Entonces el filtro debe reflejar estado seleccionado
    Y el listado debe responder visualmente a la selección
    Cuando elimino el filtro desde la interfaz
    Entonces debe volver a mostrarse sin seleccionar

  @detalle
  Escenario: Verificar tarjeta de producto y ficha de jardín
    Dado que identifico un producto de jardín con nombre visible
    Cuando abro su ficha desde la tarjeta
    Entonces el título debe corresponder al producto elegido
    Y se debe mostrar su imagen principal y sección de precio

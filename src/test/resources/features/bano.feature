# language: es

@web @qa03 @bano
Característica: Catálogo y fichas de Baño

  Antecedentes:
    Dado abrir el navegador en la url "https://www.sodimac.cl/sodimac-cl"
    Y gestiono el aviso de cookies si está visible
    Y selecciono la categoría "Baño" desde el menú principal

  @smoke
  Escenario: Explorar áreas de Baño desde el menú
    Entonces debo visualizar el contexto de la categoría "Baño"
    Y debo encontrar enlaces a tipos de productos de baño
    Cuando selecciono la opción visible "Griferías"
    Entonces debo visualizar el contexto de productos de grifería

  @filtros
  Escenario: Combinar dos filtros disponibles y limpiar la selección
    Dado que me encuentro en un listado de productos de Baño con al menos dos filtros aplicables
    Cuando selecciono una opción disponible del primer filtro
    Y selecciono una opción compatible del segundo filtro
    Entonces ambas selecciones deben observarse activas
    Y las tarjetas mostradas deben satisfacer las características visibles filtradas
    Cuando utilizo el control para limpiar filtros
    Entonces ninguna de las dos opciones debe mantenerse activa

  @detalle
  Escenario: Inspeccionar galería de una ficha de grifería
    Dado que abro desde el listado un producto de grifería con varias imágenes
    Cuando selecciono una miniatura diferente de la galería
    Entonces la imagen principal debe actualizarse visualmente
    Y el nombre del producto debe permanecer visible

  @navegacion
  Escenario: Navegar entre categoría y ficha mediante breadcrumbs
    Dado que abro una ficha de producto de Baño con ruta de navegación visible
    Cuando selecciono un enlace anterior de la ruta de navegación
    Entonces debo volver al contexto de categoría indicado por ese enlace
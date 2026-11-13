# language: es
@web @qa02 @herramientas
Característica: Exploración de Herramientas y Maquinaria
  Antecedentes:
    Dado que ingreso a la página principal de Sodimac Chile "https://www.sodimac.cl/sodimac-cl"
    Y gestiono el aviso de cookies si está visible
    Y selecciono la categoría "Herramientas y Maquinaria" desde el menú principal

  @smoke
  Escenario: Verificar menú y subcategorías de herramientas
    Entonces debo visualizar el título o contexto de "Herramientas y Maquinaria"
    Y deben existir enlaces de subcategorías interactivos
    Cuando selecciono una subcategoría de herramientas eléctricas visible
    Entonces debo visualizar un listado o portada relacionado con esa subcategoría

  @ordenamiento
  Esquema del escenario: Ordenar herramientas por precio
    Dado que estoy en un listado con al menos tres productos con precios visibles
    Cuando selecciono el ordenamiento "<orden>" en el control web
    Entonces la opción "<orden>" debe mostrarse como seleccionada
    Y los precios visibles deben cumplir el orden "<direccion>" entre productos con precio comparable
    Ejemplos:
      | orden             | direccion   |
      | Menor precio      | ascendente  |
      | Mayor precio      | descendente |

  @detalle
  Escenario: Comprobar que una herramienta conserva su identificación al abrir el detalle
    Dado que identifico una tarjeta de herramienta con nombre y precio visibles
    Cuando abro la ficha desde esa tarjeta
    Entonces el título de la ficha debe corresponder al producto seleccionado
    Y debe mostrarse una imagen principal de la herramienta
    Y el precio presentado debe ser identificable en la interfaz

  @navegacion
  Escenario: Regresar desde ficha de herramienta al listado
    Dado que he abierto una ficha desde un listado de herramientas
    Cuando regreso mediante el navegador
    Entonces debo volver al contexto del listado de herramientas
    Y deben estar disponibles los controles de exploración del listado

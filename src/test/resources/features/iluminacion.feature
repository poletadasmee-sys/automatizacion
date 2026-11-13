# language: es
@web @qa04 @iluminacion
Característica: Navegacion, filtros y visualización de Iluminación
  Antecedentes:
    Dado que ingreso a la página principal de Sodimac Chile
    Y gestiono el aviso de cookies si está visible
    Y selecciono la categoría "Iluminación" desde el menú principal

  @smoke
  Escenario: Acceder a la categoría de iluminación
    Entonces debo visualizar el título o contexto "Iluminación"
    Y debo disponer de enlaces o tarjetas de subcategoría
    Y las imágenes del contenido principal deben aparecer cargadas sin icono de error

  @filtros
  Esquema del escenario: Validar selección visual de filtros para iluminación
    Dado que accedo a un listado de lámparas con el filtro "<filtro>" visible
    Cuando marco una opción de "<filtro>" que tenga resultados
    Entonces la opción seleccionada debe mostrarse activa
    Y el catálogo debe actualizar visualmente sus resultados
    Cuando desmarco la misma opción
    Entonces debe desaparecer su estado seleccionado
    Ejemplos:
      | filtro |
      | Marca  |
      | Precio |

  @ordenamiento
  Escenario: Mantener el filtro al cambiar el orden de los resultados
    Dado que tengo una opción de filtro activa en un listado de iluminación
    Cuando selecciono un criterio de ordenamiento distinto del actual
    Entonces el criterio elegido debe quedar seleccionado
    Y la opción de filtro debe continuar activa

  @detalle
  Escenario: Validar información comercial visible en detalle de iluminación
    Dado que selecciono una lámpara disponible desde un listado
    Entonces debo visualizar el nombre de la lámpara
    Y debe mostrarse al menos una imagen del producto
    Y debe existir información de precio o disponibilidad comercial visible

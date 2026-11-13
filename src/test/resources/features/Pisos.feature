# language: es
@web @qa08 @pisos
Característica: Exploración de Pisos
  Antecedentes:
    Dado que ingreso a la página principal de Sodimac Chile
    Y gestiono el aviso de cookies si está visible
    Y selecciono la categoría "Pisos" desde el menú principal

  @smoke
  Escenario: Abrir Pisos desde el menú
    Entonces debo visualizar el contexto de "Pisos"
    Y debo encontrar enlaces de revestimientos o tipos de pisos

  @navegacion
  Escenario: Entrar a revestimientos desde la categoría
    Cuando selecciono un tipo de piso o revestimiento visible
    Entonces debo visualizar un listado relacionado con el tipo seleccionado
    Y la ruta de navegación debe identificar el contexto de la sección cuando esté presente

  @ordenamiento
  Esquema del escenario: Comprobar orden de precios visibles de pisos
    Dado que accedo a un listado de pisos con tres o más precios comparables
    Cuando selecciono el criterio "<orden>" en el selector web
    Entonces el selector debe mostrar "<orden>"
    Y los precios comparables deben quedar ordenados de forma "<direccion>"
    Ejemplos:
      | orden                   | direccion   |
      | Precio de menor a mayor | ascendente  |
      | Precio de mayor a menor | descendente |

  @detalle
  Escenario: Validar ficha de un revestimiento seleccionado
    Dado que identifico un producto de Pisos con nombre visible en el listado
    Cuando abro la ficha del producto
    Entonces debo ver el mismo nombre en el encabezado de la ficha
    Y debo visualizar una imagen del revestimiento y su precio disponible


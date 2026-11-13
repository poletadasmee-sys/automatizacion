@conexion
Feature: funcionalidad de conexion a la aplicacion


  @Login
  Scenario: Validar conexion
  Given que ingreso a la página principal de Sodimac Chile "https://www.sodimac.cl/sodimac-cl"
  When ingreso credenciales "prueba@prueba.com" y "Prueba"
  Then verifico el mensaje "Correo electrónico o contraseña incorrecta. Por favor, vuelve a intentarlo nuevamente."



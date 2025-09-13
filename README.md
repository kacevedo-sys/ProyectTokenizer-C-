# Proyecto 1 - Reconocedor de Tokens (Sección A)
**Autor:** Yonatan Boch, Lester Patricio, Kevin Acevedo, Victor Hernandez, Jose Bal 
**Asignatura:** Autómatas y Lenguajes Formales  
**Fecha de entrega:** 13/09/2025

## 1. Lenguaje de programación utilizado
- Java 17 (proyecto Maven)

## 2. Librerías o frameworks empleados
- Ninguna externa necesaria para ejecución.  
- JFlex: especificación incluida (`src/main/resources/Lexer.flex`) — opcional para regenerar el lexer.

## 3. Estructura organizada de carpetas
- `pom.xml` - Proyecto Maven.
- `src/main/java/com/unigal/lexer/` - Código Java (Main, Lexer, Token).
- `src/main/resources/Lexer.flex` - Especificación JFlex.
- `test/` - 5 archivos de prueba.
- `Salida.txt` - Ejemplo de salida generado por el proyecto.
- `README.md`, `NOTICE.txt`, `USAGE_JFLEX.txt`.

## 4. Instrucciones de instalación y uso (breves)
1. Requisitos: JDK 17+, Maven, (opcional) JFlex.
2. Importar en IntelliJ: *File → Open...* → seleccionar el `pom.xml`.
3. Ejecutar `com.unigal.lexer.Main` desde IntelliJ o:
   ```
   mvn compile exec:java -Dexec.mainClass="com.unigal.lexer.Main" -Dexec.args="test/test1.txt"
   ```
4. Seleccione el archivo de entrada (una cadena por línea). El programa genera `Salida.txt` en el directorio de ejecución.

### Regenerar `Lexer.java` con JFlex (opcional)
- Instalar JFlex y ejecutar:
  ```
  jflex -d src/main/java src/main/resources/Lexer.flex
  ```
  Esto generará una clase `JFlexLexer.java` en `src/main/java` que puede adaptarse al proyecto.

## 5. Capturas de pantalla
Incluya aquí capturas del programa en funcionamiento (se han dejado instrucciones para que el estudiante agregue imágenes reales en `docs/screenshots/` si desea).

## 6. Notas importantes
- El programa cuenta: palabras reservadas (`if`, `while`, `for`, `int`, `float`, `return`), identificadores, números, strings, llaves/paréntesis abiertos, operadores y errores (con línea y columna).
- El único reporte requerido por la cátedra es `Salida.txt`. Se incluye un ejemplo `Salida.txt`.

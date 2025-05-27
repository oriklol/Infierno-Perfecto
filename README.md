
# INFIERNO PERFECTO
## Estado actual del proyecto: Estructura y configuracion inicial del proyecto completadas
### Integrantes: Lamensa Matias - Maratea Ciro

El juego es un RPG por turnos ambientado en el infierno que cuenta Dante Alghieri en su poesia "La Divina Comedia". Los personajes deberan cargarse de fe o de pecados para destruir a sus enemigos y acabar con el Caos. Sera programado en java, utilizaremos LibGDX y se podra jugar en PC. Utilizaremos los IDE Eclipse e IntelliJ


## Enlace a la wiki: 
https://github.com/oriklol/TP-JUEGO/wiki

## Instrucciones Básicas de Compilación y Ejecución:
A continuación se detallan los pasos necesarios para clonar este repositorio y ejecutar el proyecto Java desarrollado con LibGDX y Gradle.

### 1. Clonar el Repositorio
Clonar este repositorio desde GitHub con el siguiente comando:

git clone https://github.com/oriklol/TP-JUEGO.git

Ingresar a la carpeta del proyecto:

cd TP-JUEGO

Asegúrate de tener Git instalado. Puedes verificarlo con git --version.

### 2. Verificar Instalación de Java
Este proyecto requiere Java 8 o superior. Para verificar si ya lo tenés instalado, ejecutá:

java -version

Si no está instalado, podés descargarlo desde: https://adoptium.net/

### 3. Compilar y Ejecutar el Proyecto con Gradle
Este proyecto utiliza Gradle Wrapper, por lo que no es necesario instalar Gradle por separado.

En Windows:
.\gradlew lwjgl3:run

En macOS / Linux:
./gradlew lwjgl3:run

Esto compilará y ejecutará la versión de escritorio del juego.

Si es la primera vez que se ejecuta, Gradle descargará automáticamente las dependencias necesarias. Puede tardar unos minutos.

### 4. (Opcional) Ejecutar desde un IDE
También podés abrir el proyecto en un entorno de desarrollo como IntelliJ IDEA o Eclipse.

IntelliJ IDEA:
Elegí "Open" y seleccioná la carpeta del proyecto.

IntelliJ detectará automáticamente el archivo build.gradle.kts y configurará el proyecto.

Una vez cargado, podés ejecutar el juego desde el archivo Lwjgl3Launcher.java, que se encuentra dentro del módulo lwjgl3.

Eclipse:
Ejecutá en la terminal:

./gradlew eclipse

Luego, importá el proyecto como un "Existing Projects into Workspace".



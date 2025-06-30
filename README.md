# Infierno Perfecto
## Integrantes: 
* Lamensa Matias
* Maratea Ciro

## Descripcion del proyecto:
El juego es un RPG por turnos ambientado en el infierno que cuenta Dante Alghieri en su poesia "La Divina Comedia". Los personajes deberan cargarse de fe o de pecados para destruir a sus enemigos y acabar con el Caos. Sera programado en java, utilizaremos LibGDX y se podra jugar en PC

## Enlace a la wiki del proyecto [Enlace](https://github.com/oriklol/TP-JUEGO/wiki)

## Tecnologías Utilizadas
* Framework: LibGDX
* Lenguaje: Java 17.0.6
* IDE: IntelliJ IDEA
* Plataformas objetivo: PC (Escritorio - Windows)

## Instrucciones Básicas de Compilación y Ejecución:
A continuación se detallan los pasos necesarios para que cualquier persona pueda clonar este repositorio y ejecutar el proyecto Java en su máquina local: 

Primero, se debe clonar este repositorio utilizando Git. Para ello, ejecutar el siguiente comando en la terminal:
git clone https://github.com/your-user/Infierno-Perfecto

Luego, ingresar a la carpeta del proyecto:
cd TP-JUEGO

Verificar que Java esté instalado
Asegurarse de tener instalada una versión compatible del JDK (Java Development Kit), preferentemente Java 8 o superior. Se puede verificar con el siguiente comando:
java -version

Importar el proyecto en el IDE (IntelliJ IDEA)
Buscar el archivo build.gradle en la carpeta del archivo, y elegí "Open as Project"

Para ejecutar el proyecto hay dos formas:

Desde el IDE: Dentro de IntelliJ IDEA, navegar hasta el módulo lwjgl3, encontrar la clase Lwjgl3Launcher.java y ejecutarla

Desde la consola: Para ejecutar el juego desde la terminal, utilizar el comando ./gradlew run (o gradlew.bat run en Windows)

## Estado actual del proyecto:

Se completó la configuración inicial del proyecto utilizando el framework **LibGDX**, con el lenguaje **Java 17.0.6** como base y el entorno de desarrollo **IntelliJ IDEA**.

Actualmente, el proyecto cuenta con la estructura estándar generada por el setup de LibGDX, organizada en los siguientes módulos:

- `core`: contiene la lógica principal del juego.
- `desktop`: módulo destinado a ejecutar la versión para PC (Windows).

Además, se configuró correctamente el entorno de desarrollo, incluyendo:

- Integración con **Gradle** para automatizar la compilación, ejecución y gestión de dependencias.
- Configuración del archivo `build.gradle` con las versiones adecuadas de LibGDX y otras dependencias necesarias.
- Ajustes en IntelliJ IDEA para reconocer correctamente el proyecto como multiproyecto con módulos separados.
- Ejecución exitosa de una escena o pantalla base, validando que el entorno está funcionando correctamente en plataforma de escritorio (Windows).

Con esta base, el proyecto está listo para comenzar con el desarrollo de las funcionalidades específicas del juego.


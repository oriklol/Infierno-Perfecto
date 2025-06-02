# TP-JUEGO
## Nombre del Juego: Infierno Perfecto
## Integrantes: 
* Lamensa Matias
* Maratea Ciro

## Descripcion del proyecto:
El juego es un RPG por turnos ambientado en el infierno que cuenta Dante Alghieri en su poesia "La Divina Comedia". Los personajes deberan cargarse de fe o de pecados para destruir a sus enemigos y acabar con el Caos. Sera programado en java, utilizaremos LibGDX y se podra jugar en PC

## Enlace a la wiki del proyecto [Enlace](https://github.com/oriklol/TP-JUEGO/wiki)

## Tecnologías Utilizadas
Framework: LibGDX
Lenguaje: Java 17.0.6
IDE: IntelliJ IDEA
Plataformas objetivo: PC (Escritorio - Windows)

## Instrucciones Básicas de Compilación y Ejecución:
A continuación se detallan los pasos necesarios para que cualquier persona pueda clonar este repositorio y ejecutar el proyecto Java en su máquina local:
Clonar el repositorio

Primero, se debe clonar este repositorio utilizando Git. Para ello, ejecutar el siguiente comando en la terminal:
git clone https://github.com/tu-usuario/tu-repositorio.git

Luego, ingresar a la carpeta del proyecto:
cd tu-repositorio

Verificar que Java esté instalado
Asegurarse de tener instalada una versión compatible del JDK (Java Development Kit), preferentemente Java 8 o superior. Se puede verificar con el siguiente comando:
java -version

Compilar el proyecto
Desde la raíz del proyecto, compilar los archivos .java ubicados dentro de la carpeta src (o la carpeta donde se encuentren los archivos fuente). Por ejemplo:
javac src/*.java
Esto generará archivos .class (bytecode) en la misma carpeta o en una carpeta designada.

Ejecutar el proyecto
Una vez compilado, se puede ejecutar la clase principal con el siguiente comando:
java -cp src NombreDeLaClasePrincipal
Reemplazar NombreDeLaClasePrincipal por el nombre real de la clase que contiene el método main.

## Estado actual del proyecto:
Configuración inicial y estructura del proyecto

# Infierno Perfecto

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
- `html`: Web platform using GWT and WebGL. Supports only Java projects.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `html:dist`: compiles GWT sources. The compiled application can be found at `html/build/dist`: you can use any HTTP server to deploy it.
- `html:superDev`: compiles GWT sources and runs the application in SuperDev mode. It will be available at [localhost:8080/html](http://localhost:8080/html). Use only during development.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

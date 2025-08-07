# Infierno Perfecto
## Integrantes: 
* Lamensa Matias
* Maratea Ciro

## Descripcion del proyecto:
El juego es un RPG por turnos ambientado en el infierno que cuenta Dante Alghieri en su poesia "La Divina Comedia". Los personajes deberan cargarse de fe o de pecados para destruir a sus enemigos y acabar con el Caos. Sera programado en java, utilizaremos LibGDX y se podra jugar en PC

## Enlace a la wiki del proyecto 
### [WIKI Infierno Perfecto 🎮](https://github.com/oriklol/Infierno-Perfecto/wiki/WIKI)
## Enlace a video demostracion "Primer pre-entrega" 
### [VIDEO PRIMER PRE-ENTREGA 🎥](https://youtu.be/8EgG3hR_nO8)

## Tecnologías Utilizadas
* Framework: LibGDX
* Lenguaje: Java 17.0.6
* IDE: IntelliJ IDEA
* Plataformas objetivo: PC (Escritorio - Windows)

## Instrucciones Básicas de Compilación y Ejecución
A continuación se detallan los pasos necesarios para clonar este repositorio y ejecutar correctamente el proyecto desarrollado con Java y el framework LibGDX.

### 1. Clonar el repositorio
Abrí una terminal y ejecutá:
git clone https://github.com/oriklol/Infierno-Perfecto
cd Infierno-Perfecto

### 2. Verificar que Java esté instalado
Asegurate de tener instalada una versión compatible del JDK (preferentemente Java 8 o superior). Verificá la versión con:


java -version
### 3. Importar el proyecto en el IDE
Abrí IntelliJ IDEA, seleccioná la opción "Open", y elegí el archivo build.gradle ubicado en la raíz del proyecto. IntelliJ detectará automáticamente la estructura de módulos (core, desktop, lwjgl3, etc.) y configurará el entorno de trabajo.

### 4. Ejecutar el juego
#### Opción A: Desde el IDE

En IntelliJ IDEA, navegá al módulo lwjgl3.
Abrí la clase Lwjgl3Launcher.java.
Hacé clic derecho sobre la clase y seleccioná "Run 'Lwjgl3Launcher.main()'".

#### Opción B: Desde la consola
Ejecutá el siguiente comando desde la raíz del proyecto:

./gradlew run
En Windows (si el anterior no funciona):

gradlew.bat run


## Estado actual del proyecto:

Nos encontramos en la segunda etapa del proyecto. Hemos avanzado en el codigo funcional del proyecto de manera continua durante las ultimas semanas para llegar a este estado.
Empezamos una logica de combate compleja que se va a estar resolviendo en el proximo tiempo. La gran parte de los graficos sirven para tener una idea de como van a ser las cosas. Esta primer entrega es solo la construccion del esqueleto de todo el proyecto.
Quedan por pulir muchas mecanicas dentro del juego, como tambien nuestras habilidades.

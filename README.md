# Infierno Perfecto
## Integrantes: 
* Lamensa Matias
* Maratea Ciro

## Descripcion del proyecto:
El juego es un RPG por turnos ambientado en el infierno que cuenta Dante Alghieri en su poesia "La Divina Comedia". Los personajes deberan cargarse de fe o de pecados para destruir a sus enemigos y acabar con el Caos. Sera programado en java, utilizaremos LibGDX y se podra jugar en PC

## Enlace a la wiki del proyecto 
### [WIKI Infierno Perfecto 🎮](https://github.com/oriklol/Infierno-Perfecto/wiki/)
## Enlace al CHANGELOG del proyecto 
### [CHANGELOG Infierno Perfecto 🔄](https://github.com/oriklol/Infierno-Perfecto/blob/main/CHANGELOG.md)
## Enlace a video demostracion "Segunda pre-entrega" 
### [VIDEO SEGUNDA PRE-ENTREGA 🎥](https://youtu.be/8EgG3hR_nO8)

## Tecnologías Utilizadas
* Framework: LibGDX
* Lenguaje: Java 17.0.6
* IDE: IntelliJ IDEA
* Plataformas objetivo: PC (Escritorio - Windows)

## Branches
El repositorio cuenta con tres branches (ramas) separadas de codigo. La rama "main" fue la ultima actualizacion que se hizo antes de empezar a programar la parte de red. La que se refiere a "Cliente" es la que va a usar el usuario comun que este jugando al juego, es donde se sube el codigo que va a tener el jugador en su computadora. Por ultimo la rama servidor sirve para que el Cliente pueda jugar el juego en multijugador

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

El juego se mantiene vivo gracias a la llama de los programadores. Desean llegar a completar su propuesta asi que estan mas prendidos que nunca. Actualmente el juego cuenta con una compleja logica de combates (aun en construccion), una tienda (con muchas ideas en mentes) y con una victoria. Aunque esta victoria no significa nada para el jugador, nosotros sabemos que estamos en el camino correcto. Nos vemos en las proximas actualizaciones con mas contenido.

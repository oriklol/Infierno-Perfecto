# CHANGELOG
Aqui llevaremos constancia de todas las actualizaciones y modificaciones que se hagan en el juego, tanto como en el github, como en el codigo

## [0.7.1] - 2025-11-08
### Added
- Se añadio musica de fondo en las batallas

## [0.7.0] - 2025-11-02
### Added
- Se añadieron cuatro nuevos niveles, cada uno implementado como una clase independiente.
- Se incorporaron ocho nuevos enemigos, tres minibosses y el jefe final.  
  *Los diseños están sujetos a cambios (especialmente el del jefe final, cuya forma ya se conoce pero aún no se muestra).*
- Se implementó la **Pantalla de Victoria**, que actúa como final del juego.

### Changed
- La clase **ControladorJuego** fue modificada para soportar cinco niveles en total.

---

## [0.6.2] - 2025-10-26
### Changed
- Se actualizaron los créditos iniciales.

---

## [0.6.1] - 2025-10-22
### Changed
- Se corrigió un error con el `dispose` en la tienda que impedía avanzar en el juego.
- La cantidad de usos para los objetos infinitos ya no es “-1” (implementación provisional).

### Added
- El log de combate ahora muestra qué ataque usó el enemigo, el daño infligido y cómo afecta las estadísticas (si aplica).  
  Para esto, se añadió la nueva clase **ResultadoAtaque**.
- Se agregaron verificaciones adicionales para garantizar que siempre se seleccione el enemigo a atacar.

---

## [0.6.0] - 2025-10-21
### Fixed
- Se corrigió un bug durante la batalla que hacía que la posición de los enemigos afectara su orden de muerte.  
  Ahora los enemigos se reacomodan automáticamente.

### Added
- Se añadió el jefe del primer nivel: **El Limbo**.

---

## [0.5.6] - 2025-10-18
### Changed
- El sistema de **Fe** ahora es completamente funcional.

---

## [0.5.5] - 2025-10-15
### Added
- Se añadió una **tienda** al juego, integrada dentro de la lógica del **ControladorJuego**.
- Se implementaron tres **ítems de curación**.
- Se añadió un **sistema de dinero**.

---

## [0.5.4] - 2025-10-13
### Added
- Se incorporó el sistema de **Fe** a ciertos ataques de los personajes.  
  *(Aún no es completamente utilizable.)*

---

## [0.5.2] - 2025-10-10
### Added
- Todas las pantallas ahora incluyen un método `dispose()` funcional para mejorar el rendimiento general.

---

## [0.5.1] - 2025-10-05
### Changed
- La clase **ControladorJuego** fue completamente implementada y funciona correctamente.  
  *(Aún no maneja el pasaje de niveles.)*

---

## [0.5.0] - 2025-09-30
### Added
- Se creó la clase **ControladorJuego**.  
  *(Todavía no implementada en la jugabilidad principal.)*

---

## [0.4.0] - 2025-09-23
### Added
- El jugador ahora puede morir.
- Se implementó la pantalla **Game Over**.
- Se añadió más música al juego.
- Se agregaron más **estados de batalla**, gestionados mediante *enums*.

---

## [0.3.1] - 2025-09-16
### Changed
- Se modificó la pantalla de selección de clases: ahora detecta el mouse y presenta nuevos diseños.

---

## [0.3.0] - 2025-09-08
### Fixed
- Se corrigieron los cálculos de daño.  
  Ahora todos los valores se muestran correctamente en el log de combate.

---

## [0.2.1] - 2025-09-08
### Added
- Implementación inicial del **menú de clases** (fase temprana).

---

## [0.2.0] - 2025-08-07
### Added
- Implementación del **menú principal**.
- Añadidas **opciones de configuración** (pantalla, volumen, etc.).
- Sistema de **combate básico**.
- Uso de **viewports** para ajustar la visualización.
- Implementación de **música** dentro del juego.

---

## [0.1.1] - 2025-06-02
### Changed
- Actualización del `README.md`:
  - Se agregó la sección **Tecnologías Utilizadas**.
  - Se cambió el formato del enlace a la Wiki por un hipervínculo.
  - Se modificó la presentación de los participantes del proyecto.
- Actualización del `CHANGELOG.md`:
  - Se añadieron más detalles sobre los cambios iniciales del proyecto.

---

## [0.1.0] - 2025-05-25
### Added
- Inicialización del proyecto.
- Creación de la estructura base del repositorio.
- Creación del `README.md` inicial.
- Creación de este `CHANGELOG.md`.
- Configuración inicial de la Wiki con la propuesta del proyecto.

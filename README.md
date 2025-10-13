# Proyecto Batalla Naval

## Descripción general
El proyecto **Batalla Naval** es una implementación del clásico juego de estrategia, desarrollada en **Java** bajo una arquitectura **orientada a objetos** y siguiendo los principios **SOLID**.  
El objetivo es que el jugador compita contra la máquina, ubicando barcos en un tablero y tratando de acertar las posiciones del oponente mediante disparos por turnos.

Este proyecto fue desarrollado como parte del curso impartido por la profesora **Nancy Yaneth Gélvez García** en la **Universidad Distrital Francisco José de Caldas**.

---

## Objetivos del proyecto
- Aplicar los fundamentos de la **Programación Orientada a Objetos (POO)**.  
- Implementar una arquitectura modular y mantenible siguiendo los **principios SOLID**.  
- Diseñar controladores independientes para cada componente lógico del juego.  
- Desarrollar un sistema jugable entre **usuario y máquina** con registro de partidas.  
- Documentar el sistema mediante **Javadoc** para garantizar la trazabilidad del código.

---

## Arquitectura del sistema
El sistema sigue una arquitectura basada en el **patrón MVC (Modelo-Vista-Controlador)**:

### **Controladores**
Se encargan de gestionar la lógica del juego y la interacción entre los modelos y la interfaz.  
Clases principales:
- `ControladorAtaque` – gestiona los disparos, impactos y resultados.  
- `ControladorBarcos` – maneja la ubicación y validación de barcos.  
- `ControladorLogin` / `ControladorRegistro` – gestionan la autenticación y registro de usuarios.  
- `ControladorMaquina` – controla la lógica de ataque automática.  
- `ControladorTurnos` – coordina los turnos entre jugador y máquina.  
- `GestorPersistencia` – maneja el almacenamiento y carga de información.  
- `RepositorioUsuarios` / `RepositorioPartidas` – controlan la persistencia de datos.

---

## Principios SOLID implementados
El código está diseñado aplicando las buenas prácticas de diseño orientado a objetos:

| Principio | Implementación |
|------------|----------------|
| **S** – Single Responsibility | Cada clase cumple un rol único (por ejemplo, `ControladorAtaque` solo gestiona disparos). |
| **O** – Open/Closed | Se puede ampliar la funcionalidad sin alterar el código existente (p.ej., nuevos modos de juego). |
| **L** – Liskov Substitution | Las subclases mantienen coherencia con las clases base, permitiendo intercambiabilidad. |
| **I** – Interface Segregation | Las clases implementan solo los métodos que realmente necesitan. |
| **D** – Dependency Inversion | Los controladores dependen de abstracciones, no de implementaciones directas. |

---

## Flujo de juego
1. El usuario inicia sesión o se registra.  
2. Ubica sus barcos en el tablero.  
3. El juego alterna los turnos entre jugador y máquina.  
4. Se registran los ataques y resultados.  
5. El sistema determina el ganador y guarda el historial de la partida.  

---

## Estructura del repositorio
```
BatallaNaval
 ┣ 📂 src/
 ┃ ┣ 📂 Controlador/
 ┃ ┣ 📂 Modelo/
 ┃ ┣ 📂 Vista/
 ┃ ┗ ...
 ┣ 📂 Documentacion/         ← Carpeta Javadoc generada automáticamente
 ┣ 📄 Informe_Batalla_Naval.docx
 ┣ 📄 README.md
 ┗ 📄 LICENSE
```

---

## Documentación
El proyecto incluye un **Javadoc completo** dentro de la carpeta `/Documentacion`, que detalla las clases, métodos y atributos del sistema.

Para visualizarlo:
```bash
Abrir Documentacion/index.html
```

---

## Tecnologías utilizadas
- **Lenguaje:** Java  
- **Paradigma:** Programación Orientada a Objetos  
- **Documentación:** Javadoc  
- **IDE recomendado:** IntelliJ IDEA / Eclipse  
- **Versión Java:** 17 o superior  

---

## Informe técnico
El informe técnico oficial del proyecto se encuentra en el archivo:
```
Informe_Batalla_Naval.docx
```
Allí se describe la estructura, el flujo del sistema y el diseño técnico detallado.

---

## Créditos
**Universidad Distrital Francisco José de Caldas**  
Profesora: **Nancy Yaneth Gélvez García**  
Autores: *[Espacio para nombres de los integrantes]*  

---

## Licencia
Este proyecto se distribuye con fines académicos.  
Puedes usar, estudiar y modificar el código siempre y cuando se mantenga la referencia a los autores originales.

---

⭐ *Desarrollado con fines educativos y de aprendizaje de principios SOLID en Java.*

# World Cup Explorer

Aplicación Android desarrollada en **Kotlin** con **Jetpack Compose** para explorar información del Mundial usando la API de [football-data.org](https://www.football-data.org/).

La app ya está organizada con una arquitectura **MVVM** y ahora funciona con enfoque **Offline-First**:

- La UI observa datos desde `StateFlow`.
- Los `ViewModel` dependen solo del repositorio.
- El repositorio usa **Room** como fuente local de verdad.
- **Retrofit** se usa únicamente para sincronizar datos remotos.
- Cuando vuelve la conexión, la app sincroniza automáticamente el caché local.

## Funcionalidades

- Pantalla principal con resumen del torneo.
- Lista de equipos.
- Detalle de equipo con plantilla y datos del club.
- Lista de partidos.
- Tabla de posiciones.
- Top scorers.
- Pantalla "About" con créditos de la API.
- Indicador visual de conectividad:
  - `Online`
  - `Offline - Showing cached data`

## Arquitectura

La app sigue este flujo:

`Jetpack Compose -> ViewModel -> Repository -> Room -> Retrofit`

### Capas principales

- `presentation/`
  - Pantallas Compose.
  - `ViewModel` con `StateFlow`.
  - Componentes reutilizables de UI.
- `domain/`
  - Modelos de negocio.
  - Contratos del repositorio.
  - Casos de uso.
- `data/`
  - Fuente remota con Retrofit.
  - Fuente local con Room.
  - Mapeadores entre DTO, Entity y Domain.

## Persistencia local

La app almacena en Room:

- Teams
- Matches
- Standings
- Top scorers
- Competition metadata
- Players para el detalle de equipo

Room actúa como **Single Source of Truth**. La UI no depende directamente de la red.

## Sincronización automática

La app detecta el estado de red con `ConnectivityManager`.

Cuando la conexión vuelve:

1. Se descargan los datos remotos.
2. Se actualiza Room dentro de una transacción.
3. La UI se refresca sola porque observa `Flow` desde la base local.

## Requisitos

- Android Studio reciente
- JDK 17
- Una clave de API de football-data.org

## Configuración

1. Crea o edita `local.properties`.
2. Agrega tu clave:

```properties
FOOTBALL_DATA_API_KEY=tu_clave_aqui
```

## Ejecución

Desde Android Studio:

1. Abre el proyecto.
2. Espera la sincronización de Gradle.
3. Ejecuta la app en un emulador o dispositivo.

## Verificación offline

Para probar el modo sin Internet:

1. Ejecuta la app con conexión.
2. Espera a que se carguen y guarden los datos.
3. Desactiva Internet.
4. Navega por la app.

La información debe seguir visible desde la caché local.

## Tecnologías usadas

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Hilt
- Navigation Compose
- Retrofit
- Room
- StateFlow / Flow
- Coroutines

## Estructura relevante

- `app/src/main/java/com/example/worldcupexplorer/data/local/`
- `app/src/main/java/com/example/worldcupexplorer/data/network/`
- `app/src/main/java/com/example/worldcupexplorer/data/remote/`
- `app/src/main/java/com/example/worldcupexplorer/data/repository/`
- `app/src/main/java/com/example/worldcupexplorer/domain/`
- `app/src/main/java/com/example/worldcupexplorer/presentation/`

## Nota

La app depende de la disponibilidad y límites de uso de la API de football-data.org para la sincronización remota. Si no hay conexión, el contenido local sigue funcionando.

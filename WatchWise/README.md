# watchwise-app — Frontend (.NET MAUI + SQLite)

Cliente móvil multiplataforma para **WatchWise** desarrollado con .NET MAUI. Permite gestionar watchlists, ratings y progreso de visualización sincronizándose con el backend y Trakt.tv.

---

## Tech Stack

* **.NET 9** / **.NET MAUI** (C#)
* **SQLite** - Base de datos local
* **HttpClient** - Comunicación con backend
* **MVVM Pattern** - Arquitectura recomendada para MAUI

---

## Plataformas Soportadas

* **Android** (API 21+)
* **iOS** (iOS 11+)
* **Windows** (Windows 10 1809+)
* **macOS** (macOS 10.15+)

---

## Configuración de Desarrollo

### Prerrequisitos

* **.NET 9 SDK**
* **Visual Studio 2022** o **VS Code** con extensión C#
* **MAUI workload**: `dotnet workload install maui`

### Plataformas específicas

#### Android
* **Android SDK** (API 34+)
* **Emulador Android** o dispositivo físico con depuración USB

#### iOS (solo en macOS)
* **Xcode** 15+
* **iOS Simulator** o dispositivo físico

#### Windows
* **Windows App SDK**
* **Visual Studio** con cargas de trabajo de desarrollo de Windows

---

## Compilar y Ejecutar

### Restaurar dependencias
```bash
cd watchwise-app
dotnet restore
```

### Compilar
```bash
# Todas las plataformas
dotnet build

# Plataforma específica
dotnet build -f net9.0-android
dotnet build -f net9.0-ios
```

### Ejecutar en emulador/simulador
```bash
# Android (requiere emulador activo)
dotnet build -f net9.0-android -t:Run

# iOS (solo macOS)
dotnet build -f net9.0-ios -t:Run

# Windows
dotnet run --framework net9.0-windows10.0.19041.0

# macOS
dotnet run --framework net9.0-maccatalyst
```

---

## Estructura del Proyecto

```
watchwise-app/
├─ WatchWise.App/
│  ├─ Models/              # Modelos de datos
│  ├─ ViewModels/          # ViewModels (MVVM)
│  ├─ Views/               # Páginas y controles UI
│  ├─ Services/            # Servicios (API, SQLite, etc.)
│  ├─ Platforms/           # Código específico por plataforma
│  └─ Resources/           # Recursos (imágenes, strings, etc.)
└─ WatchWise.sln           # Solución .NET
```

---

## Funcionalidades Principales

* **Autenticación** con Trakt.tv
* **Gestión de Watchlist** (agregar/quitar títulos)
* **Ratings y Reviews** de películas/series
* **Progreso de visualización** con sincronización
* **Cache local** con SQLite para uso offline
* **Búsqueda** de contenido
* **Perfil de usuario** y configuraciones

---

## Configuración

La aplicación utiliza `appsettings.json` para configuración:

```json
{
  "ApiSettings": {
    "BaseUrl": "https://api.watchwise.app",
    "Timeout": 30
  },
  "TraktSettings": {
    "ClientId": "your-trakt-client-id"
  }
}
```

Variables de entorno para desarrollo:
- `WATCHWISE_API_URL` - URL del backend local (ej: `http://localhost:8080`)
- `TRAKT_CLIENT_ID` - ID de cliente de Trakt.tv

---

## Testing

```bash
# Ejecutar tests unitarios
cd watchwise-app
dotnet test
```

---

## Deployment

### Android
```bash
dotnet publish -f net9.0-android -c Release
```

### iOS
```bash
dotnet publish -f net9.0-ios -c Release
```

### Windows
```bash
dotnet publish -f net9.0-windows10.0.19041.0 -c Release
```

---

## License

[MIT License](../LICENSE)
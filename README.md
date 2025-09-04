# WatchWise — Client para Trakt.tv

Monorepo que contiene el cliente móvil y la API backend para **WatchWise**, una aplicación que integra con Trakt.tv para gestionar watchlists, ratings y progreso de visualización.

---

## 📁 Estructura del Proyecto

```
/ (root)
│  .gitignore                # Configuración git para .NET, Java, Gradle, IDEs
│  README.md                 # Este archivo
│  LICENSE                   # Licencia MIT
│  .github/workflows/        # Pipelines CI/CD
│
├─ WatchWise/                 # .NET MAUI (C#) + SQLite
│  ├─ WatchWise.App/         # Proyecto principal MAUI
│  ├─ WatchWise.sln          # Solución .NET
│  └─ README.md              # Documentación específica del frontend
│
└─ watch-wise-api/           # Spring Boot (Java 21) + Gradle + PostgreSQL
   ├─ src/                   # Código fuente
   ├─ build.gradle           # Configuración de dependencias
   ├─ gradlew               # Gradle wrapper
   └─ README.md             # Documentación específica del backend
```

---

## 🚀 Inicio Rápido

### Prerrequisitos

- **Frontend**: .NET 9 + MAUI workload
- **Backend**: JDK 21 + Docker (PostgreSQL/Redis)

### Ejecutar el Frontend (.NET MAUI)

```bash
cd WatchWise
dotnet restore
dotnet build

# Para Android (requiere emulador)
dotnet build -f net9.0-android

# Para Windows/macOS
dotnet run
```

### Ejecutar el Backend (Spring Boot)

```bash
cd watch-wise-api

# Levantar servicios de base de datos
docker compose up -d postgres redis

# Ejecutar aplicación
./gradlew bootRun
```

**Backend**: [http://localhost:8080](http://localhost:8080)
**Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔧 Desarrollo

### CI/CD

- **Pipeline Frontend**: Se ejecuta solo con cambios en `WatchWise/**`
- **Pipeline Backend**: Se ejecuta solo con cambios en `watch-wise-api/**`

### Testing

```bash
# Frontend
cd WatchWise
dotnet test

# Backend
cd watch-wise-api
./gradlew test
```

---

## 📋 Stack Tecnológico

### Frontend (WatchWise)
- **.NET MAUI** (C#) - Framework multiplataforma
- **SQLite** - Base de datos local
- **HttpClient** - Comunicación con backend

### Backend (watch-wise-api)
- **Java 21** / **Spring Boot 3.4+**
- **Gradle** - Build tool
- **PostgreSQL** - Base de datos principal
- **Redis** - Cache
- **Spring Security** - Autenticación JWT
- **OpenAPI/Swagger** - Documentación API

---

## 🎯 Funcionalidades

- **Autenticación** con Trakt.tv
- **Sincronización** de watchlists, ratings y progreso
- **Cache inteligente** de metadatos
- **Disponibilidad** por país/plataforma
- **Interfaz nativa** para Android, iOS, Windows, macOS

---

## 📝 Licencia

[MIT License](LICENSE) - Copyright (c) 2025


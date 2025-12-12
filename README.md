# WatchWise — Client para Trakt.tv

Monorepo que contiene el cliente móvil y la API backend para **WatchWise**, una aplicación que integra con Trakt.tv para gestionar watchlists, ratings y progreso de visualización.

---

## Architecture

**Hexagonal / Clean Architecture**:

* **Controllers**: REST entrypoints
* **Services**: business orchestration
* **UseCases**: domain logic
* **Repositories**: persistence abstractions
* **Infra**: Postgres, Redis, external API clients

---

## API Surface (MVP)

* **Auth**
  * `POST /auth/register` → register user
  * `POST /auth/login` → JWT issuance
* **Titles**
  * `GET /titles/{id}` → fetch metadata snapshot
  * `GET /titles?query=` → search titles
* **Availability**
  * `GET /availability/{id}?country=XX` → providers by country
* **Sync**
  * `POST /sync` → batch up user changes (watchlist, ratings, progress)
  * `GET /me/state?since=` → deltas since timestamp

---

## Persistence Model

* `User` (id, email, passwordHash, roles)
* `UserWatchlist` (userId, canonicalId, addedAt)
* `UserRating` (userId, canonicalId, rating, ratedAt)
* `UserProgress` (userId, canonicalId, progressSeconds, updatedAt)
* `TitleCache` (canonicalId, title, year, type, posterUrl, overview, genres, runtime, ratingAvg, lastSyncedAt, expiresAt, rawPayload)
* `AvailabilitySnapshot` (canonicalId, countryCode, provider, offerType, price, currency, deepLink, collectedAt, expiresAt)

---

## Caching Strategy

* **Titles**: TTL 7–30d (shorter for popular, longer for long-tail)
* **Availability**: TTL 24–72h
* **Redis**: query results & hot objects

---

## Jobs & Scheduling

* **Popular refresh**: every 6–12h
* **New releases**: daily
* **Availability refresh**: every 24–72h
* **On-demand refresh** when snapshot expired on request

---

## Security

* JWT stateless auth with roles
* Spring Security filter chain (custom JWT filter)
* Swagger/OpenAPI under `permitAll`
* Global CORS config
* GDPR/CCPA endpoints: account deletion

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


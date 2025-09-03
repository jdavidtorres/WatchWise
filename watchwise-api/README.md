# watchwise-api — Backend (Spring Boot + PostgreSQL + Redis)

Backend service for **WatchWise.App**. Provides user management, metadata cache, availability snapshots, and sync endpoints integrating external APIs (Trakt, JustWatch).

---

## Tech Stack

* **Java 21** / **Spring Boot 3.4+**
* **Gradle** (Groovy DSL)
* **PostgreSQL** (persistent store)
* **Redis** (hot cache)
* **Spring Data JPA** + Hibernate
* **Spring Security** (JWT stateless)
* **Spring Validation**
* **MapStruct** (mappers)
* **Flyway** (DB migrations)
* **Testcontainers** (integration testing)
* **OpenAPI/Swagger** (API docs via SpringDoc 2.8.9)

---

## Local Dev Setup

### Requirements

* JDK 21+
* Docker (for Postgres + Redis)
* Gradle 8+

### Run services

```bash
cd watchwise-api
docker compose up -d postgres redis
```

### Run app

```bash
cd watchwise-api
./gradlew bootRun
```

Backend available at [http://localhost:8080](http://localhost:8080)

Swagger UI at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Testing

* **Unit tests**: JUnit 5 + AssertJ (mocked dependencies, no in-memory DB)
* **Integration tests**: SpringBootTest + Testcontainers (Postgres, Redis) — optional
* **Contract tests**: OpenAPI validation
* Coverage target: ≥85% line, ≥50% branch

```bash
cd watchwise-api
./gradlew test
```

---

## API Surface (MVP)

* **Health**: `GET /actuator/health` → service status
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

## Architecture

**Hexagonal / Clean Architecture**:

* **Controllers**: REST entrypoints
* **Services**: business orchestration
* **UseCases**: domain logic
* **Repositories**: persistence abstractions
* **Infra**: Postgres, Redis, external API clients

---

## Security

* JWT stateless auth with roles
* Spring Security filter chain (custom JWT filter)
* Swagger/OpenAPI under `permitAll`
* Global CORS config
* GDPR/CCPA endpoints: account deletion

---

## License

[MIT License](../LICENSE)
# watch-wise-api — Backend (Spring Boot + PostgreSQL + Redis)

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
* **Titles** 🆕
  * `GET /api/titles/search?q=&type=&page=` → search titles with filtering and pagination
  * `GET /api/titles/{id}` → get detailed title information
* **Watchlist**
  * `GET /watchlist` → get user's watchlist (requires auth)
  * `POST /watchlist/{canonicalId}` → add to watchlist (requires auth)
  * `DELETE /watchlist/{canonicalId}` → remove from watchlist (requires auth)
* **Availability**
  * `GET /availability/{id}?country=XX` → providers by country
* **Sync**
  * `POST /sync` → batch up user changes (watchlist, ratings, progress)
  * `GET /me/state?since=` → deltas since timestamp

---

## 🔎 Testing the Search Functionality

The MVP implementation includes title search and detail endpoints with a fake data provider containing popular movies and TV shows.

### Quick API Tests

```bash
# Search for movies/shows
curl "http://localhost:8080/api/titles/search?q=matrix"
curl "http://localhost:8080/api/titles/search?q=breaking&type=SHOW"
curl "http://localhost:8080/api/titles/search?q=the&type=MOVIE&page=0"

# Get title details
curl "http://localhost:8080/api/titles/tt0133093"  # The Matrix
curl "http://localhost:8080/api/titles/tt0903747"  # Breaking Bad

# Test error handling
curl "http://localhost:8080/api/titles/nonexistent"  # Returns 404
```

### Available Test Data

The in-memory provider includes these titles:
- **Movies**: The Shawshank Redemption, The Godfather, The Dark Knight, Interstellar, The Matrix, Schindler's List
- **TV Shows**: Breaking Bad, Game of Thrones

### Frontend Integration

The .NET MAUI frontend includes:
- **Models**: `TitleType`, `TitleLite`, `TitleDetail` matching backend DTOs
- **Services**: `IApiClient`, `ApiClient` with structured API calls
- **ViewModels**: `SearchViewModel`, `TitleDetailViewModel`, `WatchlistViewModel` with MVVM pattern
- **UI**: Updated MainPage with search, results display, and watchlist functionality

Run the frontend demo:
```bash
cd WatchWise
./frontend-demo.sh
```

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

## Observability

* Spring Boot Actuator (`/actuator/health`, `/metrics`)
* Micrometer → Prometheus/Grafana
* Logs structured JSON (traceId)
* Metrics: error rate, cache hit ratio, latency, job success rate

---

## Local Dev Setup

### Requirements

* JDK 21+
* Docker (for Postgres + Redis)
* Gradle 8+

### Run services

```bash
cd watch-wise-api
docker compose up -d postgres redis
```

### Run app

```bash
cd watch-wise-api
gradle bootRun
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
cd watch-wise-api
gradle test
```

---

## Deployment

* Containerized with Docker
* CI/CD pipeline: build → test → publish Docker image → deploy (staging/prod)
* Config via environment variables (see `application.yml`)

---

## Roadmap (Backend‑only excerpts)

* Index titles in Elasticsearch/OpenSearch for advanced search
* Add notifications (new episodes, availability changes)
* WebSockets/SSE for realtime sync events
* Fine-grained roles/permissions (admin, support)

---

## License

TBD (MIT/Apache‑2.0).


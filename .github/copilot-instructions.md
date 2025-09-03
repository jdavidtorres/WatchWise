# WatchWise Copilot Instructions

This repository contains **WatchWise**, a streaming content discovery and management platform with a Spring Boot API backend and .NET MAUI mobile app frontend.

## Repository Structure

```
WatchWise/
├── watch-wise-api/          # Spring Boot REST API backend
│   ├── src/main/java/       # Java source code
│   ├── src/test/java/       # Unit tests
│   ├── build.gradle         # Gradle build configuration
│   └── docker-compose.yml   # Local development services
├── WatchWise/               # .NET MAUI cross-platform mobile app
│   ├── WatchWise.App/       # Main MAUI application
│   └── WatchWise.sln        # Visual Studio solution
├── .github/workflows/       # CI/CD pipelines
│   ├── api-ci.yml          # Spring Boot API pipeline
│   └── app-ci.yml          # MAUI app pipeline
└── README.md               # Project documentation
```

## Backend API (`watch-wise-api/`)

### Technology Stack
- **Java 21** with **Spring Boot 3.4+**
- **Gradle** (Groovy DSL) for builds
- **PostgreSQL** for persistent storage
- **Redis** for caching
- **Spring Security** with JWT stateless authentication
- **Spring Data JPA** + Hibernate for ORM
- **MapStruct** for object mapping
- **Flyway** for database migrations
- **OpenAPI/Swagger** for API documentation
- **Testcontainers** for integration testing

### Architecture
**Hexagonal/Clean Architecture** with clear separation:
- **Controllers**: REST endpoints (`@RestController`)
- **Services**: Business logic orchestration (`@Service`)
- **UseCases**: Domain logic (pure business rules)
- **Repositories**: Data persistence abstractions (`@Repository`)
- **Infrastructure**: External integrations (Postgres, Redis, Trakt API, JustWatch API)

### Code Patterns
- Use constructor injection (avoid `@Autowired` on fields)
- Follow Spring Boot conventions for package structure: `com.watchwise.{feature}`
- Controllers should be thin - delegate to services
- Use record classes for DTOs and request/response objects
- Implement proper exception handling with `@ControllerAdvice`
- Use `@Transactional` on service methods that modify data

### Testing Guidelines
- **Unit Tests**: JUnit 5 + AssertJ + Mockito
- Mock all external dependencies (no in-memory databases for unit tests)
- Test classes should end with `Test` (e.g., `UserServiceTest`)
- Use `@ExtendWith(MockitoExtension.class)` for Mockito tests
- **Integration Tests**: Use `@SpringBootTest` + Testcontainers sparingly
- **Coverage Targets**: ≥85% line coverage, ≥50% branch coverage
- Run tests with: `cd watch-wise-api && gradle test`

### Security
- JWT stateless authentication with role-based authorization
- Use `Authentication` parameter in controllers to get current user
- Swagger/OpenAPI endpoints are publicly accessible
- CORS configured globally

### API Design
- RESTful endpoints following OpenAPI 3 specification
- Use appropriate HTTP status codes
- Include `@Operation` annotations for Swagger documentation
- Consistent error response format
- Pagination for list endpoints

### Build & Development
- **Local setup**: Requires JDK 21+, Docker for services
- **Start services**: `docker compose up -d postgres redis`
- **Run application**: `gradle bootRun`
- **API available at**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## Frontend App (`WatchWise/`)

### Technology Stack
- **.NET MAUI** for cross-platform mobile development
- **C# 12** with .NET 9
- **XAML** for UI definition
- Cross-platform targets: iOS, Android, Windows, macOS

### Architecture
- MVVM pattern with data binding
- Platform-specific implementations in `Platforms/` folder
- Shared business logic in main project

### Build & Development
- **Requirements**: .NET 9 SDK, MAUI workload
- **Restore**: `dotnet restore WatchWise/WatchWise.sln`
- **Build**: `dotnet build WatchWise/WatchWise.sln`

## Development Workflow

### Branch Strategy
- Feature branches: `feature/feature-name`
- Bug fixes: `fix/bug-description`
- Hotfixes: `hotfix/issue-description`

### Code Quality
- Follow established patterns in existing codebase
- Write tests for new functionality
- Ensure CI pipelines pass before merging
- Use meaningful commit messages

### CI/CD
- **API CI**: Triggered on changes to `watch-wise-api/`
- **App CI**: Triggered on changes to `WatchWise/`
- Both pipelines run on pull requests and pushes

## Key Domain Concepts

### Business Logic
- **Users**: Authentication, profiles, preferences
- **Titles**: Movies/TV shows with metadata caching
- **Watchlists**: User's saved content for later viewing
- **Lists**: Custom user-created collections
- **Progress**: Episode/movie viewing progress tracking
- **Availability**: Streaming provider information by region
- **Sync**: Integration with external services (Trakt, JustWatch)

### Data Model
- `User` - user accounts with email/password
- `UserWatchlist` - titles user wants to watch
- `UserRating` - user ratings for titles
- `UserProgress` - viewing progress tracking
- `TitleCache` - cached metadata from external APIs
- `AvailabilitySnapshot` - streaming availability by provider/region

## External Integrations
- **Trakt API**: User data sync, ratings, watchlists
- **JustWatch API**: Streaming availability data
- **TMDB/OMDB**: Title metadata and images

## Common Tasks

### Adding a New Feature
1. Create feature branch
2. Add service layer with business logic
3. Add controller with REST endpoints
4. Write unit tests for service and controller
5. Update OpenAPI documentation
6. Test with local development setup

### Database Changes
1. Create Flyway migration in `src/main/resources/db/migration/`
2. Follow naming convention: `V{version}__{description}.sql`
3. Test migration with local PostgreSQL

### Adding Dependencies
1. Add to `build.gradle` dependencies block
2. Follow Spring Boot starter conventions where possible
3. Document any configuration requirements

## Code Style
- Follow Java naming conventions (camelCase, PascalCase for classes)
- Use meaningful variable and method names
- Keep methods focused and concise
- Add JavaDoc for public APIs
- Use final keyword for immutable variables
- Prefer composition over inheritance

Remember: This is a **multi-technology** repository. Always specify which part (API/App) you're working on and use the appropriate tools and patterns for each platform.
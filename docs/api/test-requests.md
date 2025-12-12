# WatchWise API Test Requests

This file contains sample HTTP requests for testing the WatchWise API endpoints.

## Title Search

### Search for "matrix"
```http
GET http://localhost:8080/api/titles/search?q=matrix
```

### Search for movies only
```http
GET http://localhost:8080/api/titles/search?q=the&type=MOVIE
```

### Search for TV shows only
```http
GET http://localhost:8080/api/titles/search?q=breaking&type=SHOW
```

### Search with pagination
```http
GET http://localhost:8080/api/titles/search?q=the&page=1
```

## Title Details

### Get details for The Matrix
```http
GET http://localhost:8080/api/titles/tt0133093
```

### Get details for Breaking Bad
```http
GET http://localhost:8080/api/titles/tt0903747
```

### Test 404 for non-existent title
```http
GET http://localhost:8080/api/titles/nonexistent
```

## Using curl

```bash
# Search for titles
curl "http://localhost:8080/api/titles/search?q=matrix"

# Get title details
curl "http://localhost:8080/api/titles/tt0133093"

# Search with type filter
curl "http://localhost:8080/api/titles/search?q=breaking&type=SHOW"
```
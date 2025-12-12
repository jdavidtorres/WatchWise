package co.com.jdti.titles;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Titles", description = "Title search and detail operations")
@RestController
@RequestMapping("/api/titles")
@Validated
public class TitleController {

    private final TitleService titleService;

    public TitleController(TitleService titleService) {
        this.titleService = titleService;
    }

    @Operation(
        summary = "Search titles by query",
        description = "Search for movies and TV shows by name. Returns paginated results."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successful search",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TitleLiteDto.class),
                examples = @ExampleObject(
                    value = "[{\"id\":\"tt0111161\",\"type\":\"MOVIE\",\"name\":\"The Shawshank Redemption\",\"year\":1994,\"posterUrl\":\"https://example.com/poster.jpg\"}]"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/search")
    public List<TitleLiteDto> search(
        @Parameter(description = "Search query", required = true)
        @RequestParam("q") 
        @Size(min=2, message="Query must be at least 2 characters")
        String query,
        
        @Parameter(description = "Filter by title type")
        @RequestParam(value = "type", required = false) 
        TitleType type,
        
        @Parameter(description = "Page number (0-based)")
        @RequestParam(value = "page", defaultValue = "0") 
        @Min(value = 0, message = "Page must be >= 0") 
        int page
    ) {
        return titleService.search(query, type, page);
    }

    @Operation(
        summary = "Get title details",
        description = "Get detailed information for a specific title by ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Title found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TitleDetailDto.class),
                examples = @ExampleObject(
                    value = "{\"id\":\"tt0111161\",\"type\":\"MOVIE\",\"name\":\"The Shawshank Redemption\",\"year\":1994,\"overview\":\"Two imprisoned men bond...\",\"runtime\":142,\"genres\":[\"Drama\"],\"posterUrl\":\"https://example.com/poster.jpg\",\"backdropUrl\":\"https://example.com/backdrop.jpg\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Title not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TitleDetailDto> getDetails(
        @Parameter(description = "Title identifier", required = true)
        @PathVariable String id
    ) {
        return titleService.getDetails(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}

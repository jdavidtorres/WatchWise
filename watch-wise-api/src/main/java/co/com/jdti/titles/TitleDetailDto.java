package co.com.jdti.titles;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Detailed title information")
public record TitleDetailDto(
    @Schema(description = "Unique identifier for the title", example = "tt0111161")
    String id,
    
    @Schema(description = "Type of title (movie or show)")
    TitleType type,
    
    @Schema(description = "Title name", example = "The Shawshank Redemption")
    String name,
    
    @Schema(description = "Release year", example = "1994")
    Integer year,
    
    @Schema(description = "Plot overview")
    String overview,
    
    @Schema(description = "Runtime in minutes", example = "142")
    Integer runtime,
    
    @Schema(description = "List of genres")
    List<String> genres,
    
    @Schema(description = "URL to poster image")
    String posterUrl,
    
    @Schema(description = "URL to backdrop image")
    String backdropUrl
) {}
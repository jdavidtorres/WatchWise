package co.com.jdti.titles;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lightweight title information for search results")
public record TitleLiteDto(
    @Schema(description = "Unique identifier for the title", example = "tt0111161")
    String id,
    
    @Schema(description = "Type of title (movie or show)")
    TitleType type,
    
    @Schema(description = "Title name", example = "The Shawshank Redemption")
    String name,
    
    @Schema(description = "Release year", example = "1994")
    Integer year,
    
    @Schema(description = "URL to poster image")
    String posterUrl
) {}
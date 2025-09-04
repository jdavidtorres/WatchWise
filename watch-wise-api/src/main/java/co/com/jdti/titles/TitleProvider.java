package co.com.jdti.titles;

import java.util.List;
import java.util.Optional;

/**
 * Interface for providing title data from external sources
 */
public interface TitleProvider {
    
    /**
     * Search for titles by query
     * @param query Search query
     * @param type Optional title type filter
     * @param page Page number (0-based)
     * @param pageSize Number of results per page
     * @return List of matching titles
     */
    List<TitleLiteDto> search(String query, TitleType type, int page, int pageSize);
    
    /**
     * Get detailed information for a specific title
     * @param id Title identifier
     * @return Title details if found
     */
    Optional<TitleDetailDto> getDetails(String id);
}
package com.watchwise.titles;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitleService {
    
    private final TitleProvider titleProvider;
    
    public TitleService(TitleProvider titleProvider) {
        this.titleProvider = titleProvider;
    }
    
    /**
     * Search for titles
     * @param query Search query (minimum 2 characters)
     * @param type Optional title type filter
     * @param page Page number (0-based)
     * @return List of matching titles
     */
    public List<TitleLiteDto> search(String query, TitleType type, int page) {
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }
        
        int pageSize = 20; // Default page size
        return titleProvider.search(query.trim(), type, page, pageSize);
    }
    
    /**
     * Get detailed information for a specific title
     * @param id Title identifier
     * @return Title details if found
     */
    public Optional<TitleDetailDto> getDetails(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return titleProvider.getDetails(id.trim());
    }
}
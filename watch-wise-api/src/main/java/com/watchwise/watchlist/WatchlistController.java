package com.watchwise.watchlist;

import com.watchwise.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Watchlist")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final UserRepository userRepository;

    public WatchlistController(WatchlistService watchlistService, UserRepository userRepository) {
        this.watchlistService = watchlistService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Get watchlist")
    @GetMapping
    public List<String> get(Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        return watchlistService.getWatchlist(userId);
    }

    @Operation(summary = "Follow title")
    @PostMapping("/{canonicalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void follow(@PathVariable String canonicalId, Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        watchlistService.follow(userId, canonicalId);
    }

    @Operation(summary = "Unfollow title")
    @DeleteMapping("/{canonicalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollow(@PathVariable String canonicalId, Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        watchlistService.unfollow(userId, canonicalId);
    }
}

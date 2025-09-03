package com.watchwise.watchlist;

import com.watchwise.domain.UserWatchlist;
import com.watchwise.domain.UserWatchlistId;
import com.watchwise.repository.UserWatchlistRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class WatchlistService {

    private final UserWatchlistRepository watchlistRepository;

    public WatchlistService(UserWatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public List<String> getWatchlist(UUID userId) {
        return watchlistRepository.findByIdUserId(userId).stream()
                .map(w -> w.getId().getCanonicalId())
                .toList();
    }

    public void follow(UUID userId, String canonicalId) {
        var id = new UserWatchlistId(userId, canonicalId);
        if (!watchlistRepository.existsById(id)) {
            var watch = new UserWatchlist();
            watch.setId(id);
            watch.setAddedAt(Instant.now());
            watchlistRepository.save(watch);
        }
    }

    public void unfollow(UUID userId, String canonicalId) {
        watchlistRepository.deleteById(new UserWatchlistId(userId, canonicalId));
    }
}

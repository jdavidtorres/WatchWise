package com.watchwise.repository;

import com.watchwise.domain.UserWatchlist;
import com.watchwise.domain.UserWatchlistId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserWatchlistRepository extends JpaRepository<UserWatchlist, UserWatchlistId> {
    List<UserWatchlist> findByIdUserId(UUID userId);
}

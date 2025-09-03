package com.watchwise.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user_watchlist")
@Data
@NoArgsConstructor
public class UserWatchlist {

    @EmbeddedId
    private UserWatchlistId id;

    @Column(nullable = false)
    private Instant addedAt;
}

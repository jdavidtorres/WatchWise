package com.watchwise.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "availability_snapshot")
@Data
@NoArgsConstructor
public class AvailabilitySnapshot {

    @EmbeddedId
    private AvailabilitySnapshotId id;

    private BigDecimal price;

    private String currency;

    private String deepLink;

    @Column(nullable = false)
    private Instant collectedAt;

    private Instant expiresAt;
}

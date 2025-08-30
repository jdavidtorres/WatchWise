package com.watchwise.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilitySnapshotId implements Serializable {
    private String canonicalId;
    private String countryCode;
    private String provider;
    private String offerType;
}

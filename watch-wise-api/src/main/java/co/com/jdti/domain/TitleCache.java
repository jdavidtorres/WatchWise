package co.com.jdti.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "title_cache")
@Data
@NoArgsConstructor
public class TitleCache {

    @Id
    private String canonicalId;

    private String title;

    private Integer year;

    private String type;

    private String posterUrl;

    @Column(length = 2000)
    private String overview;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "title_cache_genres", joinColumns = @JoinColumn(name = "canonical_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    private Integer runtime;

    private Double ratingAvg;

    private Instant lastSyncedAt;

    private Instant expiresAt;

    @Lob
    private String rawPayload;
}

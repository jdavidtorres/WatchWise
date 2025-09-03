package co.com.jdti.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user_progress")
@Data
@NoArgsConstructor
public class UserProgress {

    @EmbeddedId
    private UserProgressId id;

    @Column
    private Integer season;

    @Column
    private Integer episode;

    @Column(nullable = false)
    private int progressSeconds;

    @Column(nullable = false)
    private Instant updatedAt;
}

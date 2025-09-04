package co.com.jdti.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user_rating")
@Data
@NoArgsConstructor
public class UserRating {

    @EmbeddedId
    private UserRatingId id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private Instant ratedAt;
}

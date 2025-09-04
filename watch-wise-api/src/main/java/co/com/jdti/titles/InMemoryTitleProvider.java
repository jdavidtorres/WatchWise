package co.com.jdti.titles;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * In-memory implementation of TitleProvider with fake data for MVP testing
 */
@Component
public class InMemoryTitleProvider implements TitleProvider {
    
    private static final List<TitleDetailDto> FAKE_TITLES = List.of(
        new TitleDetailDto(
            "tt0111161",
            TitleType.MOVIE,
            "The Shawshank Redemption",
            1994,
            "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
            142,
            List.of("Drama"),
            "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg"
        ),
        new TitleDetailDto(
            "tt0068646",
            TitleType.MOVIE,
            "The Godfather",
            1972,
            "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
            175,
            List.of("Crime", "Drama"),
            "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
        ),
        new TitleDetailDto(
            "tt0468569",
            TitleType.MOVIE,
            "The Dark Knight",
            2008,
            "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests.",
            152,
            List.of("Action", "Crime", "Drama"),
            "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg"
        ),
        new TitleDetailDto(
            "tt0816692",
            TitleType.MOVIE,
            "Interstellar",
            2014,
            "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
            169,
            List.of("Adventure", "Drama", "Sci-Fi"),
            "https://m.media-amazon.com/images/M/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg"
        ),
        new TitleDetailDto(
            "tt0903747",
            TitleType.SHOW,
            "Breaking Bad",
            2008,
            "A high school chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine.",
            47,
            List.of("Crime", "Drama", "Thriller"),
            "https://m.media-amazon.com/images/M/MV5BMjhiMzgxZTctNDc1Ni00OTIxLTlhMTYtZTA3ZWFkODRkNmFmXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BMjhiMzgxZTctNDc1Ni00OTIxLTlhMTYtZTA3ZWFkODRkNmFmXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
        ),
        new TitleDetailDto(
            "tt0944947",
            TitleType.SHOW,
            "Game of Thrones",
            2011,
            "Nine noble families wage war against each other in order to gain control over the mythical land of Westeros.",
            57,
            List.of("Action", "Adventure", "Drama"),
            "https://m.media-amazon.com/images/M/MV5BYTRiNDQwYzAtMzVlZS00NTI5LWJjYjUtMzkwNTUzMWMxZTllXkEyXkFqcGdeQXVyNDIzMzcwNjc@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BYTRiNDQwYzAtMzVlZS00NTI5LWJjYjUtMzkwNTUzMWMxZTllXkEyXkFqcGdeQXVyNDIzMzcwNjc@._V1_.jpg"
        ),
        new TitleDetailDto(
            "tt0133093",
            TitleType.MOVIE,
            "The Matrix",
            1999,
            "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
            136,
            List.of("Action", "Sci-Fi"),
            "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg"
        ),
        new TitleDetailDto(
            "tt0108052",
            TitleType.MOVIE,
            "Schindler's List",
            1993,
            "In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce.",
            195,
            List.of("Biography", "Drama", "History"),
            "https://m.media-amazon.com/images/M/MV5BNDE4OTMxMTctNmRhYy00NWE2LTg3YzItYTk3M2UwOTU5Njg4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BNDE4OTMxMTctNmRhYy00NWE2LTg3YzItYTk3M2UwOTU5Njg4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg"
        )
    );
    
    private static final Map<String, TitleDetailDto> TITLES_BY_ID = FAKE_TITLES.stream()
        .collect(java.util.stream.Collectors.toMap(TitleDetailDto::id, t -> t));

    @Override
    public List<TitleLiteDto> search(String query, TitleType type, int page, int pageSize) {
        // Filter titles by query and type
        List<TitleDetailDto> filtered = FAKE_TITLES;
        if (query != null && !query.trim().isEmpty()) {
            String lowerQuery = query.toLowerCase().trim();
            filtered = filtered.stream()
                .filter(title -> title.name().toLowerCase().contains(lowerQuery))
                .toList();
        }
        if (type != null) {
            filtered = filtered.stream()
                .filter(title -> title.type() == type)
                .toList();
        }
        // Calculate pagination indices
        int start = Math.max(0, page * pageSize);
        int end = Math.min(filtered.size(), start + pageSize);
        if (start >= filtered.size()) {
            return List.of();
        }
        return filtered.subList(start, end).stream()
            .map(this::toTitleLite)
            .toList();
    }

    @Override
    public Optional<TitleDetailDto> getDetails(String id) {
        return Optional.ofNullable(TITLES_BY_ID.get(id));
    }
    
    private TitleLiteDto toTitleLite(TitleDetailDto detail) {
        return new TitleLiteDto(
            detail.id(),
            detail.type(),
            detail.name(),
            detail.year(),
            detail.posterUrl()
        );
    }
}
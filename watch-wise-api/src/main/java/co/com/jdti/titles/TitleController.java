package co.com.jdti.titles;

import co.com.jdti.trakt.TraktApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Titles")
@RestController
@RequestMapping("/titles")
public class TitleController {

    private final TraktApiClient traktApiClient;

    public TitleController(TraktApiClient traktApiClient) {
        this.traktApiClient = traktApiClient;
    }

    @Operation(summary = "Search titles by query")
    @GetMapping
    public String search(@RequestParam String query) {
        return traktApiClient.searchMovie(query);
    }
}

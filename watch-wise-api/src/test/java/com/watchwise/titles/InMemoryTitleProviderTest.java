package com.watchwise.titles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTitleProviderTest {

    InMemoryTitleProvider provider;

    @BeforeEach
    void setup() {
        provider = new InMemoryTitleProvider();
    }

    @Test
    void searchReturnsResultsForValidQuery() {
        List<TitleLiteDto> results = provider.search("Matrix", null, 0, 10);

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).name()).contains("Matrix");
    }

    @Test
    void searchReturnsEmptyForNoMatches() {
        List<TitleLiteDto> results = provider.search("NonexistentMovie", null, 0, 10);

        assertThat(results).isEmpty();
    }

    @Test
    void searchFiltersByType() {
        List<TitleLiteDto> results = provider.search("", TitleType.MOVIE, 0, 10);

        assertThat(results).allMatch(title -> title.type() == TitleType.MOVIE);
    }

    @Test
    void searchPaginatesResults() {
        List<TitleLiteDto> page1 = provider.search("", null, 0, 3);
        List<TitleLiteDto> page2 = provider.search("", null, 1, 3);

        assertThat(page1).hasSize(3);
        assertThat(page2).isNotEmpty();
        assertThat(page1).doesNotContainAnyElementsOf(page2);
    }

    @Test
    void getDetailsReturnsExistingTitle() {
        Optional<TitleDetailDto> result = provider.getDetails("tt0111161");

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("The Shawshank Redemption");
    }

    @Test
    void getDetailsReturnsEmptyForNonexistentTitle() {
        Optional<TitleDetailDto> result = provider.getDetails("nonexistent");

        assertThat(result).isEmpty();
    }
}
package co.com.jdti.titles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitleServiceTest {

    @Mock
    TitleProvider titleProvider;

    TitleService titleService;

    @BeforeEach
    void setup() {
        titleService = new TitleService(titleProvider);
    }

    @Test
    void searchDelegatesToProvider() {
        TitleLiteDto expectedTitle = new TitleLiteDto("tt123", TitleType.MOVIE, "Test Movie", 2023, "poster.jpg");
        when(titleProvider.search("test", null, 0, 20)).thenReturn(List.of(expectedTitle));

        List<TitleLiteDto> result = titleService.search("test", null, 0);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(expectedTitle);
        verify(titleProvider).search("test", null, 0, 20);
    }

    @Test
    void searchTrimsQuery() {
        TitleLiteDto expectedTitle = new TitleLiteDto("tt123", TitleType.MOVIE, "Test Movie", 2023, "poster.jpg");
        when(titleProvider.search("test", TitleType.MOVIE, 1, 20)).thenReturn(List.of(expectedTitle));

        List<TitleLiteDto> result = titleService.search("  test  ", TitleType.MOVIE, 1);

        assertThat(result).hasSize(1);
        verify(titleProvider).search("test", TitleType.MOVIE, 1, 20);
    }

    @Test
    void searchReturnsEmptyListForShortQuery() {
        List<TitleLiteDto> result = titleService.search("a", null, 0);

        assertThat(result).isEmpty();
        verifyNoInteractions(titleProvider);
    }

    @Test
    void searchReturnsEmptyListForNullQuery() {
        List<TitleLiteDto> result = titleService.search(null, null, 0);

        assertThat(result).isEmpty();
        verifyNoInteractions(titleProvider);
    }

    @Test
    void getDetailsDelegatesToProvider() {
        TitleDetailDto expectedDetail = new TitleDetailDto("tt123", TitleType.MOVIE, "Test Movie", 2023,
            "Overview", 120, List.of("Drama"), "poster.jpg", "backdrop.jpg");
        when(titleProvider.getDetails("tt123")).thenReturn(Optional.of(expectedDetail));

        Optional<TitleDetailDto> result = titleService.getDetails("tt123");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedDetail);
        verify(titleProvider).getDetails("tt123");
    }

    @Test
    void getDetailsTrimsId() {
        TitleDetailDto expectedDetail = new TitleDetailDto("tt123", TitleType.MOVIE, "Test Movie", 2023,
            "Overview", 120, List.of("Drama"), "poster.jpg", "backdrop.jpg");
        when(titleProvider.getDetails("tt123")).thenReturn(Optional.of(expectedDetail));

        Optional<TitleDetailDto> result = titleService.getDetails("  tt123  ");

        assertThat(result).isPresent();
        verify(titleProvider).getDetails("tt123");
    }

    @Test
    void getDetailsReturnsEmptyForNullId() {
        Optional<TitleDetailDto> result = titleService.getDetails(null);

        assertThat(result).isEmpty();
        verifyNoInteractions(titleProvider);
    }

    @Test
    void getDetailsReturnsEmptyForEmptyId() {
        Optional<TitleDetailDto> result = titleService.getDetails("  ");

        assertThat(result).isEmpty();
        verifyNoInteractions(titleProvider);
    }
}
package com.watchwise.titles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitleControllerTest {

    @Mock
    TitleService titleService;

    TitleController controller;

    @BeforeEach
    void setup() {
        controller = new TitleController(titleService);
    }

    @Test
    void searchDelegatesToService() {
        TitleLiteDto expectedTitle = new TitleLiteDto("tt123", TitleType.MOVIE, "Test Movie", 2023, "poster.jpg");
        when(titleService.search("inception", null, 0)).thenReturn(List.of(expectedTitle));

        List<TitleLiteDto> response = controller.search("inception", null, 0);

        assertThat(response).hasSize(1);
        assertThat(response.get(0)).isEqualTo(expectedTitle);
        verify(titleService).search("inception", null, 0);
    }

    @Test
    void searchWithTypeFilter() {
        TitleLiteDto expectedTitle = new TitleLiteDto("tt123", TitleType.SHOW, "Test Show", 2023, "poster.jpg");
        when(titleService.search("test", TitleType.SHOW, 1)).thenReturn(List.of(expectedTitle));

        List<TitleLiteDto> response = controller.search("test", TitleType.SHOW, 1);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).type()).isEqualTo(TitleType.SHOW);
        verify(titleService).search("test", TitleType.SHOW, 1);
    }

    @Test
    void getDetailsReturnsTitle() {
        TitleDetailDto expectedDetail = new TitleDetailDto("tt123", TitleType.MOVIE, "Test Movie", 2023, 
            "Overview", 120, List.of("Drama"), "poster.jpg", "backdrop.jpg");
        when(titleService.getDetails("tt123")).thenReturn(Optional.of(expectedDetail));

        ResponseEntity<TitleDetailDto> response = controller.getDetails("tt123");

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(expectedDetail);
        verify(titleService).getDetails("tt123");
    }

    @Test
    void getDetailsReturnsNotFoundWhenTitleDoesNotExist() {
        when(titleService.getDetails("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<TitleDetailDto> response = controller.getDetails("nonexistent");

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isNull();
        verify(titleService).getDetails("nonexistent");
    }
}

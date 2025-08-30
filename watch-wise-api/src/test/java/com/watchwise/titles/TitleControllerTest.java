package com.watchwise.titles;

import com.watchwise.trakt.TraktApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitleControllerTest {

    @Mock
    TraktApiClient traktApiClient;

    TitleController controller;

    @BeforeEach
    void setup() {
        controller = new TitleController(traktApiClient);
    }

    @Test
    void searchDelegatesToClient() {
        when(traktApiClient.searchMovie("inception")).thenReturn("[]");

        String response = controller.search("inception");

        assertThat(response).isEqualTo("[]");
        verify(traktApiClient).searchMovie("inception");
    }
}

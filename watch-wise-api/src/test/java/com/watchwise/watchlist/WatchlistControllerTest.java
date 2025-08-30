package com.watchwise.watchlist;

import com.watchwise.domain.User;
import com.watchwise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchlistControllerTest {

    @Mock
    WatchlistService watchlistService;
    @Mock
    UserRepository userRepository;

    WatchlistController controller;

    @BeforeEach
    void setup() {
        controller = new WatchlistController(watchlistService, userRepository);
    }

    @Test
    void getDelegatesToService() {
        UUID userId = UUID.randomUUID();
        var u = new User();
        u.setId(userId);
        when(userRepository.findByEmail("follower@example.com")).thenReturn(Optional.of(u));
        when(watchlistService.getWatchlist(userId)).thenReturn(List.of("t1"));

        Authentication auth = new UsernamePasswordAuthenticationToken("follower@example.com", null);
        var result = controller.get(auth);
        assertThat(result).containsExactly("t1");
    }

    @Test
    void followCallsService() {
        UUID userId = UUID.randomUUID();
        var u = new User();
        u.setId(userId);
        when(userRepository.findByEmail("follower@example.com")).thenReturn(Optional.of(u));

        Authentication auth = new UsernamePasswordAuthenticationToken("follower@example.com", null);
        controller.follow("t1", auth);

        verify(watchlistService).follow(userId, "t1");
    }

    @Test
    void unfollowCallsService() {
        UUID userId = UUID.randomUUID();
        var u = new User();
        u.setId(userId);
        when(userRepository.findByEmail("follower@example.com")).thenReturn(Optional.of(u));

        Authentication auth = new UsernamePasswordAuthenticationToken("follower@example.com", null);
        controller.unfollow("t1", auth);

        verify(watchlistService).unfollow(userId, "t1");
    }
}

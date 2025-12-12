package co.com.jdti.watchlist;

import co.com.jdti.domain.UserWatchlist;
import co.com.jdti.domain.UserWatchlistId;
import co.com.jdti.repository.UserWatchlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchlistServiceTest {

    @Mock
    UserWatchlistRepository watchlistRepository;
    @InjectMocks
    WatchlistService watchlistService;

    @Test
    void followSkipsExisting() {
        UUID userId = UUID.randomUUID();
        String canonical = "m1";
        UserWatchlistId id = new UserWatchlistId(userId, canonical);
        when(watchlistRepository.existsById(id)).thenReturn(true);

        watchlistService.follow(userId, canonical);

        verify(watchlistRepository, never()).save(any());
    }

    @Test
    void followSavesWhenMissing() {
        UUID userId = UUID.randomUUID();
        String canonical = "m1";
        UserWatchlistId id = new UserWatchlistId(userId, canonical);
        when(watchlistRepository.existsById(id)).thenReturn(false);

        watchlistService.follow(userId, canonical);

        verify(watchlistRepository).save(any(UserWatchlist.class));
    }

    @Test
    void getWatchlistReturnsIds() {
        UUID userId = UUID.randomUUID();
        UserWatchlist uw = new UserWatchlist();
        uw.setId(new UserWatchlistId(userId, "m1"));
        when(watchlistRepository.findByIdUserId(userId)).thenReturn(List.of(uw));

        List<String> ids = watchlistService.getWatchlist(userId);
        assertThat(ids).containsExactly("m1");
    }

    @Test
    void unfollowDeletes() {
        UUID userId = UUID.randomUUID();
        watchlistService.unfollow(userId, "m1");
        verify(watchlistRepository).deleteById(new UserWatchlistId(userId, "m1"));
    }
}

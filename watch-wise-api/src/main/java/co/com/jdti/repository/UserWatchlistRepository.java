package co.com.jdti.repository;

import co.com.jdti.domain.UserWatchlist;
import co.com.jdti.domain.UserWatchlistId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserWatchlistRepository extends JpaRepository<UserWatchlist, UserWatchlistId> {
    List<UserWatchlist> findByIdUserId(UUID userId);
}

package co.com.jdti.repository;

import co.com.jdti.domain.UserProgress;
import co.com.jdti.domain.UserProgressId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProgressRepository extends JpaRepository<UserProgress, UserProgressId> {
}

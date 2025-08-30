package com.watchwise.repository;

import com.watchwise.domain.UserProgress;
import com.watchwise.domain.UserProgressId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProgressRepository extends JpaRepository<UserProgress, UserProgressId> {
}

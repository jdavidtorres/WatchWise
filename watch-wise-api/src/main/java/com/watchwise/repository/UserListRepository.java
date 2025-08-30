package com.watchwise.repository;

import com.watchwise.domain.UserList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserListRepository extends JpaRepository<UserList, UUID> {
    List<UserList> findByUserId(UUID userId);
}

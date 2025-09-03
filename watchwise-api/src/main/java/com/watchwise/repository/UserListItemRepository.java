package com.watchwise.repository;

import com.watchwise.domain.UserListItem;
import com.watchwise.domain.UserListItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserListItemRepository extends JpaRepository<UserListItem, UserListItemId> {
    List<UserListItem> findByIdListId(UUID listId);
}

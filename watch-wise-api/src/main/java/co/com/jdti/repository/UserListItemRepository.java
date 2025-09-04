package co.com.jdti.repository;

import co.com.jdti.domain.UserListItem;
import co.com.jdti.domain.UserListItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserListItemRepository extends JpaRepository<UserListItem, UserListItemId> {
    List<UserListItem> findByIdListId(UUID listId);
}

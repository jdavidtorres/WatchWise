package com.watchwise.lists;

import com.watchwise.domain.UserList;
import com.watchwise.domain.UserListItem;
import com.watchwise.domain.UserListItemId;
import com.watchwise.repository.UserListItemRepository;
import com.watchwise.repository.UserListRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ListService {

    private final UserListRepository listRepository;
    private final UserListItemRepository itemRepository;

    public ListService(UserListRepository listRepository, UserListItemRepository itemRepository) {
        this.listRepository = listRepository;
        this.itemRepository = itemRepository;
    }

    public List<UserList> getLists(UUID userId) {
        return listRepository.findByUserId(userId);
    }

    public UserList create(UUID userId, String name) {
        var list = new UserList();
        list.setUserId(userId);
        list.setName(name);
        list.setCreatedAt(Instant.now());
        return listRepository.save(list);
    }

    public void delete(UUID userId, UUID listId) {
        var list = listRepository.findById(listId).orElseThrow();
        if (!list.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not owner");
        }
        itemRepository.deleteAll(itemRepository.findByIdListId(listId));
        listRepository.delete(list);
    }

    public List<String> getItems(UUID userId, UUID listId) {
        var list = listRepository.findById(listId).orElseThrow();
        if (!list.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not owner");
        }
        return itemRepository.findByIdListId(listId).stream()
                .map(i -> i.getId().getCanonicalId())
                .toList();
    }

    public void addItem(UUID userId, UUID listId, String canonicalId) {
        var list = listRepository.findById(listId).orElseThrow();
        if (!list.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not owner");
        }
        var id = new UserListItemId(listId, canonicalId);
        if (!itemRepository.existsById(id)) {
            var item = new UserListItem();
            item.setId(id);
            item.setAddedAt(Instant.now());
            itemRepository.save(item);
        }
    }

    public void removeItem(UUID userId, UUID listId, String canonicalId) {
        var list = listRepository.findById(listId).orElseThrow();
        if (!list.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not owner");
        }
        itemRepository.deleteById(new UserListItemId(listId, canonicalId));
    }
}

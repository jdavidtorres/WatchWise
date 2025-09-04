package co.com.jdti.lists;

import co.com.jdti.domain.UserList;
import co.com.jdti.domain.UserListItem;
import co.com.jdti.domain.UserListItemId;
import co.com.jdti.repository.UserListItemRepository;
import co.com.jdti.repository.UserListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListServiceTest {

    @Mock
    UserListRepository listRepository;
    @Mock
    UserListItemRepository itemRepository;
    @InjectMocks
    ListService listService;

    @Test
    void deleteFailsForNonOwner() {
        UUID listId = UUID.randomUUID();
        UserList list = new UserList();
        list.setId(listId);
        list.setUserId(UUID.randomUUID());
        when(listRepository.findById(listId)).thenReturn(Optional.of(list));

        assertThrows(IllegalArgumentException.class, () -> listService.delete(UUID.randomUUID(), listId));
    }

    @Test
    void createSavesList() {
        UUID userId = UUID.randomUUID();
        UserList list = new UserList();
        list.setId(UUID.randomUUID());
        when(listRepository.save(any(UserList.class))).thenReturn(list);

        UserList saved = listService.create(userId, "Favs");
        assertThat(saved).isEqualTo(list);
        verify(listRepository).save(any(UserList.class));
    }

    @Test
    void getListsDelegatesToRepository() {
        UUID userId = UUID.randomUUID();
        listService.getLists(userId);
        verify(listRepository).findByUserId(userId);
    }

    @Test
    void getItemsReturnsCanonicalIds() {
        UUID userId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        UserList list = new UserList();
        list.setId(listId);
        list.setUserId(userId);
        when(listRepository.findById(listId)).thenReturn(Optional.of(list));
        UserListItem item = new UserListItem();
        item.setId(new UserListItemId(listId, "m1"));
        when(itemRepository.findByIdListId(listId)).thenReturn(List.of(item));

        List<String> items = listService.getItems(userId, listId);
        assertThat(items).containsExactly("m1");
    }

    @Test
    void addItemSavesWhenMissing() {
        UUID userId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        UserList list = new UserList();
        list.setId(listId);
        list.setUserId(userId);
        when(listRepository.findById(listId)).thenReturn(Optional.of(list));
        UserListItemId id = new UserListItemId(listId, "m1");
        when(itemRepository.existsById(id)).thenReturn(false);

        listService.addItem(userId, listId, "m1");
        verify(itemRepository).save(any(UserListItem.class));
    }

    @Test
    void removeItemDeletes() {
        UUID userId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        UserList list = new UserList();
        list.setId(listId);
        list.setUserId(userId);
        when(listRepository.findById(listId)).thenReturn(Optional.of(list));

        listService.removeItem(userId, listId, "m1");
        verify(itemRepository).deleteById(new UserListItemId(listId, "m1"));
    }
}

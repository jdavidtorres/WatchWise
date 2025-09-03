package com.watchwise.lists;

import com.watchwise.domain.User;
import com.watchwise.domain.UserList;
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
class ListControllerTest {

    @Mock
    ListService listService;
    @Mock
    UserRepository userRepository;

    ListController controller;

    @BeforeEach
    void setup() {
        controller = new ListController(listService, userRepository);
    }

    @Test
    void createReturnsListId() {
        UUID userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        when(userRepository.findByEmail("lister@example.com")).thenReturn(Optional.of(user));

        var list = new UserList();
        list.setId(UUID.randomUUID());
        list.setUserId(userId);
        list.setName("Favs");
        when(listService.create(userId, "Favs")).thenReturn(list);

        Authentication auth = new UsernamePasswordAuthenticationToken("lister@example.com", null);
        var response = controller.create(new ListController.CreateListRequest("Favs"), auth);
        assertThat(response.id()).isEqualTo(list.getId());
        assertThat(response.name()).isEqualTo("Favs");
        verify(listService).create(userId, "Favs");
    }

    @Test
    void listsReturnsUserLists() {
        UUID userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        when(userRepository.findByEmail("lister@example.com")).thenReturn(Optional.of(user));

        var list = new UserList();
        list.setId(UUID.randomUUID());
        list.setUserId(userId);
        list.setName("Favs");
        when(listService.getLists(userId)).thenReturn(List.of(list));

        Authentication auth = new UsernamePasswordAuthenticationToken("lister@example.com", null);
        var response = controller.lists(auth);
        assertThat(response).hasSize(1);
        assertThat(response.get(0).id()).isEqualTo(list.getId());
        verify(listService).getLists(userId);
    }

    @Test
    void addItemDelegatesToService() {
        UUID userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        when(userRepository.findByEmail("lister@example.com")).thenReturn(Optional.of(user));

        Authentication auth = new UsernamePasswordAuthenticationToken("lister@example.com", null);
        UUID listId = UUID.randomUUID();
        controller.addItem(listId, "m1", auth);
        verify(listService).addItem(userId, listId, "m1");
    }

    @Test
    void deleteCallsService() {
        UUID userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        when(userRepository.findByEmail("lister@example.com")).thenReturn(Optional.of(user));

        Authentication auth = new UsernamePasswordAuthenticationToken("lister@example.com", null);
        UUID listId = UUID.randomUUID();
        controller.delete(listId, auth);
        verify(listService).delete(userId, listId);
    }

    @Test
    void itemsReturnsIds() {
        UUID userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        when(userRepository.findByEmail("lister@example.com")).thenReturn(Optional.of(user));

        UUID listId = UUID.randomUUID();
        when(listService.getItems(userId, listId)).thenReturn(List.of("m1"));

        Authentication auth = new UsernamePasswordAuthenticationToken("lister@example.com", null);
        var response = controller.items(listId, auth);
        assertThat(response.items()).containsExactly("m1");
    }

    @Test
    void removeItemCallsService() {
        UUID userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        when(userRepository.findByEmail("lister@example.com")).thenReturn(Optional.of(user));

        Authentication auth = new UsernamePasswordAuthenticationToken("lister@example.com", null);
        UUID listId = UUID.randomUUID();
        controller.removeItem(listId, "m1", auth);
        verify(listService).removeItem(userId, listId, "m1");
    }
}

package com.watchwise.lists;

import com.watchwise.domain.UserList;
import com.watchwise.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Lists")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/lists")
public class ListController {

    private final ListService listService;
    private final UserRepository userRepository;

    public ListController(ListService listService, UserRepository userRepository) {
        this.listService = listService;
        this.userRepository = userRepository;
    }

    record CreateListRequest(String name) {}
    record ListResponse(UUID id, String name) {}
    record ItemsResponse(List<String> items) {}

    @Operation(summary = "Get user lists")
    @GetMapping
    public List<ListResponse> lists(Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        return listService.getLists(userId).stream()
                .map(l -> new ListResponse(l.getId(), l.getName()))
                .toList();
    }

    @Operation(summary = "Create list")
    @PostMapping
    public ListResponse create(@RequestBody CreateListRequest request, Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        UserList list = listService.create(userId, request.name());
        return new ListResponse(list.getId(), list.getName());
    }

    @Operation(summary = "Delete list")
    @DeleteMapping("/{listId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID listId, Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        listService.delete(userId, listId);
    }

    @Operation(summary = "List items")
    @GetMapping("/{listId}")
    public ItemsResponse items(@PathVariable UUID listId, Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        return new ItemsResponse(listService.getItems(userId, listId));
    }

    @Operation(summary = "Add item")
    @PostMapping("/{listId}/items/{canonicalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addItem(@PathVariable UUID listId, @PathVariable String canonicalId, Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        listService.addItem(userId, listId, canonicalId);
    }

    @Operation(summary = "Remove item")
    @DeleteMapping("/{listId}/items/{canonicalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable UUID listId, @PathVariable String canonicalId, Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        listService.removeItem(userId, listId, canonicalId);
    }
}

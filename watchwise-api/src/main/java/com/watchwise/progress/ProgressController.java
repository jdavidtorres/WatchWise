package com.watchwise.progress;

import com.watchwise.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Tag(name = "Progress")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService progressService;
    private final UserRepository userRepository;

    public ProgressController(ProgressService progressService, UserRepository userRepository) {
        this.progressService = progressService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Get progress")
    @GetMapping("/{canonicalId}")
    public ProgressDto get(@PathVariable String canonicalId, Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        var progress = progressService.getProgress(userId, canonicalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ProgressDto(progress.getSeason(), progress.getEpisode(), progress.getProgressSeconds(), progress.getUpdatedAt());
    }

    @Operation(summary = "Update progress")
    @PutMapping("/{canonicalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String canonicalId, @RequestBody ProgressRequest request, Authentication authentication) {
        UUID userId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        progressService.updateProgress(userId, canonicalId, request.season(), request.episode(), request.progressSeconds());
    }

    public record ProgressRequest(Integer season, Integer episode, int progressSeconds) {}

    public record ProgressDto(Integer season, Integer episode, int progressSeconds, Instant updatedAt) {}
}

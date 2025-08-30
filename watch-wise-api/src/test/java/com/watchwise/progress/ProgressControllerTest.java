package com.watchwise.progress;

import com.watchwise.domain.User;
import com.watchwise.domain.UserProgress;
import com.watchwise.domain.UserProgressId;
import com.watchwise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressControllerTest {

    @Mock
    ProgressService progressService;
    @Mock
    UserRepository userRepository;

    ProgressController controller;

    @BeforeEach
    void setup() {
        controller = new ProgressController(progressService, userRepository);
    }

    @Test
    void getReturnsProgress() {
        UUID userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        when(userRepository.findByEmail("viewer@example.com")).thenReturn(Optional.of(user));

        var progress = new UserProgress();
        progress.setId(new UserProgressId(userId, "t1"));
        progress.setSeason(1);
        progress.setEpisode(2);
        progress.setProgressSeconds(90);
        progress.setUpdatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        when(progressService.getProgress(userId, "t1")).thenReturn(Optional.of(progress));

        Authentication auth = new UsernamePasswordAuthenticationToken("viewer@example.com", null);
        var dto = controller.get("t1", auth);
        assertThat(dto.season()).isEqualTo(1);
        assertThat(dto.episode()).isEqualTo(2);
        assertThat(dto.progressSeconds()).isEqualTo(90);
    }

    @Test
    void updateDelegatesToService() {
        UUID userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        when(userRepository.findByEmail("viewer@example.com")).thenReturn(Optional.of(user));

        Authentication auth = new UsernamePasswordAuthenticationToken("viewer@example.com", null);
        var request = new ProgressController.ProgressRequest(1, 2, 90);
        controller.update("t1", request, auth);

        verify(progressService).updateProgress(userId, "t1", 1, 2, 90);
    }
}

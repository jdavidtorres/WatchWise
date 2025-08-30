package com.watchwise.progress;

import com.watchwise.domain.UserProgress;
import com.watchwise.domain.UserProgressId;
import com.watchwise.repository.UserProgressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    UserProgressRepository progressRepository;
    @InjectMocks
    ProgressService progressService;

    @Test
    void getDelegatesToRepository() {
        UUID userId = UUID.randomUUID();
        String canonical = "t1";
        progressService.getProgress(userId, canonical);
        verify(progressRepository).findById(new UserProgressId(userId, canonical));
    }

    @Test
    void updateInsertsNewProgress() {
        UUID userId = UUID.randomUUID();
        String canonical = "t1";
        when(progressRepository.findById(any())).thenReturn(Optional.empty());

        progressService.updateProgress(userId, canonical, 1, 2, 90);

        verify(progressRepository).save(argThat(p ->
                p.getId().equals(new UserProgressId(userId, canonical)) &&
                        p.getSeason().equals(1) &&
                        p.getEpisode().equals(2) &&
                        p.getProgressSeconds() == 90));
    }
}

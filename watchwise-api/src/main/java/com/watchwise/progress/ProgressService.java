package com.watchwise.progress;

import com.watchwise.domain.UserProgress;
import com.watchwise.domain.UserProgressId;
import com.watchwise.repository.UserProgressRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProgressService {

    private final UserProgressRepository progressRepository;

    public ProgressService(UserProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    public Optional<UserProgress> getProgress(UUID userId, String canonicalId) {
        return progressRepository.findById(new UserProgressId(userId, canonicalId));
    }

    public void updateProgress(UUID userId, String canonicalId, Integer season, Integer episode, int progressSeconds) {
        var id = new UserProgressId(userId, canonicalId);
        var progress = progressRepository.findById(id).orElseGet(() -> {
            var p = new UserProgress();
            p.setId(id);
            return p;
        });
        progress.setSeason(season);
        progress.setEpisode(episode);
        progress.setProgressSeconds(progressSeconds);
        progress.setUpdatedAt(Instant.now());
        progressRepository.save(progress);
    }
}

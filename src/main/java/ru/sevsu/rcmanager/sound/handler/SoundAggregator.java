package ru.sevsu.rcmanager.sound.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoundAggregator {

    @Autowired
    private List<Handler> handlers;

    public void handleSound(byte[] sound) {
        Instant time = Instant.now();
        log.debug("Audio start handle in " + time);
        List<CompletableFuture<Void>> handlerFutures = handlers.stream()
                        .map(h -> CompletableFuture.runAsync(() -> h.handle(sound)))
                .collect(Collectors.toList());

        CompletableFuture<Void> allOf = CompletableFuture.allOf(handlerFutures.toArray(new CompletableFuture[0]));
        allOf.thenRun(() -> {
            log.debug("Audio started handle it " + time + " ended in " + Instant.now());
        });

    }
}

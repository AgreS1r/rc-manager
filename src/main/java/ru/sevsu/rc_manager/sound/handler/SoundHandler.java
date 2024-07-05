package ru.sevsu.rc_manager.sound.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sevsu.rc_manager.sound.processor.SoundConverter;

import javax.sound.sampled.AudioInputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoundHandler {
    private final SaveSoundHandler saveSoundHandler;
    private final RecognizeHandler recognizeHandler;
    private final TelegramSendHandler telegramSendHandler;


// This handler convert byte array to AudioStream and send it to other handlers
    public void handle(byte[] sound) {
        Handler[] handlers = {
                saveSoundHandler,
                recognizeHandler,
                telegramSendHandler
        };
        log.info("Audio start handle");
        CompletableFuture<Void>[] futures = new CompletableFuture[handlers.length];
        for (int i = 0; i < handlers.length; i++) {
            int index = i;
            futures[i] = CompletableFuture.runAsync(() -> handlers[index].handle(sound));
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures);

        allOf.thenRun(() -> {
            log.info("Audio handled");
        });
    }
}

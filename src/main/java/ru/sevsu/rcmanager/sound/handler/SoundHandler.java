package ru.sevsu.rcmanager.sound.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoundHandler {
    private final SaveSoundHandler saveSoundHandler;
    private final TelegramSendHandler telegramSendHandler;
    private final RecognizeHandler recognizeHandler;


    public void handle(byte[] sound) {
        Instant time = Instant.now();
        log.debug("Audio start handle in " + time);
        saveSoundHandler.handle(sound);
        telegramSendHandler.handle(sound);
        recognizeHandler.handle(sound);
        log.debug("Audio started handle it " + time + " ended in " + Instant.now());
    }
}

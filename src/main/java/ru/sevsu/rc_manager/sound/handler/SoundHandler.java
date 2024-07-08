package ru.sevsu.rc_manager.sound.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sevsu.rc_manager.sound.processor.SoundConverter;

import javax.sound.sampled.AudioInputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoundHandler {
    private final SaveHandler saveHandler;
    private final SoundConverter soundConverter;
    private final RecognizeHandler recognizeHandler;


    public void handle(byte[] sound) {
        log.info("Audio handled");
        saveHandler.handle(sound);
        recognizeHandler.handle(sound);

    }
}

package ru.sevsu.rc_manager.sound.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.sevsu.rc_manager.sound.processor.SoundConverter;

import javax.sound.sampled.AudioInputStream;

@Component
public class SoundHandler {
    private static final Logger log = LoggerFactory.getLogger(SoundHandler.class);
    private final SaveHandler saveHandler;
    private final SoundConverter soundConverter;

    public SoundHandler(SaveHandler saveHandler, SoundConverter soundConverter) {
        this.saveHandler = saveHandler;
        this.soundConverter = soundConverter;
    }

    public void handle(byte[] sound) {
        log.info("Audio handled");
        AudioInputStream audioInputStream = soundConverter.byteToStream(sound);
        saveHandler.handle(audioInputStream);
    }
}

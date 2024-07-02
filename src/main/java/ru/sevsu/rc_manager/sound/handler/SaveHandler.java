package ru.sevsu.rc_manager.sound.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SaveHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(SaveHandler.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss");

    @Override
    public void handle(AudioInputStream audioInputStream) {
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
                    new File(LocalDateTime.now().format(formatter) + "_signal.wav"));
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
}

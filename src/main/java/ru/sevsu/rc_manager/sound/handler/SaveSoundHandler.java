package ru.sevsu.rc_manager.sound.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Handler saves sound on device named by received date and time
@Component
@Slf4j
public class SaveSoundHandler implements Handler {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss");
    @Value("${sound.save-path}")
    private String savePath;
    @Override
    public void handle(AudioInputStream audioInputStream) {
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
                    new File(savePath + "/" + LocalDateTime.now().format(formatter) + "_signal.wav"));
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
}

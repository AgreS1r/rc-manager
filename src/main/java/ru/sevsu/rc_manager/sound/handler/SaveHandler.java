package ru.sevsu.rc_manager.sound.handler;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
@Slf4j
public class SaveHandler implements Handler {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss");

    @Override
    public void handle(AudioInputStream audioInputStream) {
        String audioFilePath = "temp.wav";
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
                    new File(audioFilePath));
        } catch (IOException e) {
            log.warn(e.getMessage());
        }


    }
}



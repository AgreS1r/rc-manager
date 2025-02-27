package ru.sevsu.rcmanager.sound.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.sevsu.rcmanager.sound.processor.SoundConverter;

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
@RequiredArgsConstructor
@ConditionalOnProperty(value = "handler.save", havingValue = "true")
public class SaveSoundHandler implements Handler {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss");
    private final SoundConverter soundConverter;

    @Value("${sound.save-path}")
    private String savePath;

    @Override
    public void handle(byte[] sound) {

        String dateTimePath = LocalDateTime.now().format(formatter) + "_signal.wav";

        try {
            AudioInputStream audioInputStream = soundConverter.byteToStream(sound);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
                    new File(savePath + "/" + dateTimePath));
        } catch (IOException e) {
            log.warn("Error saving audio file: {}", e.getMessage());
        }
    }
}

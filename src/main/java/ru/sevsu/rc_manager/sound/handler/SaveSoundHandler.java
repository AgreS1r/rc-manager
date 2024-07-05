package ru.sevsu.rc_manager.sound.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sevsu.rc_manager.sound.processor.SoundConverter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Handler saves sound on device named by received date and time
@Component
@RequiredArgsConstructor
@Slf4j
public class SaveSoundHandler implements Handler {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss");
    private final SoundConverter soundConverter;

    @Override
    public void handle(byte[] sound) {
        AudioInputStream audioInputStream = soundConverter.byteToStream(sound);
        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
                    new File(LocalDateTime.now().format(formatter) + "_signal.wav"));
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
        log.info(SaveSoundHandler.class.toString() + "handled");
    }
}

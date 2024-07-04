package ru.sevsu.rc_manager.sound.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class SaveHandler implements Handler {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss");

    @Override
    public void handle(AudioInputStream audioInputStream) {
        // Original path with "temp.wav"
        String audioFilePath = "temp.wav";

        // New path with date and time appended
        String dateTimePath = LocalDateTime.now().format(formatter) + "_signal.wav";

        try {
            // Save the audio file with the original path
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(audioFilePath));
        } catch (IOException e) {
            log.warn("Error saving audio file: {}", e.getMessage());
        }
        try {
            // Read the original file
            Path sourcePath = Paths.get(audioFilePath);

            // Create a new file with the date appended
            Path targetPath = Paths.get(dateTimePath);

            // Copy the contents from the original file to the new file
            Files.copy(sourcePath, targetPath, StandardCopyOption.COPY_ATTRIBUTES);

            log.info("Copied {} to {}", audioFilePath, dateTimePath);
        } catch (IOException e) {
            log.warn("Error copying audio file: {}", e.getMessage(), e);
        }
    }
}
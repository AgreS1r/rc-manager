package ru.sevsu.rcmanager.sound.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
public class SoundConverter {
    private final SoundFormat soundFormat;

    public AudioInputStream byteToStream(byte[] sound) {
        log.debug("Converting byte array to AudioInputStream...");
        AudioFormat format = soundFormat.getAudioFormat();
        log.debug("Creating ByteArrayInputStream from byte array...");
        ByteArrayInputStream byas = new ByteArrayInputStream(sound);
        log.debug("Creating AudioInputStream from ByteArrayInputStream and AudioFormat...");
        return new AudioInputStream(byas, format, sound.length);
    }
}

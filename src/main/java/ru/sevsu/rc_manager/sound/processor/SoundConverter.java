package ru.sevsu.rc_manager.sound.processor;

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
        AudioFormat format = soundFormat.getAudioFormat();
        ByteArrayInputStream byas = new ByteArrayInputStream(sound);
        return new AudioInputStream(byas, format, sound.length);
    }
}

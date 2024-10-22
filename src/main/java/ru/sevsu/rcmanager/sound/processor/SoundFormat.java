package ru.sevsu.rcmanager.sound.processor;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;

@Getter
@Component
public class SoundFormat {
    private final AudioFormat audioFormat;

    public SoundFormat(@Value("${sound.sample-rate}") float sampleRate,
                       @Value("${sound.sample-size-in-bits}") int sampleSizeInBits,
                       @Value("${sound.channels}") int channels,
                       @Value("${sound.signed}") boolean signed,
                       @Value("${sound.big-endian}") boolean bigEndian) {
        audioFormat =
                new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }


}

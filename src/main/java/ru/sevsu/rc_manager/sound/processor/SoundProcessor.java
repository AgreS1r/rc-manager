package ru.sevsu.rc_manager.sound.processor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SoundProcessor {
    private final double amplitudeThreshold;
    private final int sampleSize;

    public SoundProcessor(@Value("${sound.amplitude-threshold}") double amplitudeThreshold,
                          @Value("${sound.sample-size}") int sampleSize) {
        this.amplitudeThreshold = amplitudeThreshold;
        this.sampleSize = sampleSize;
    }

    public boolean isSound(byte[] buffer) {

        for (int i = 0; i < buffer.length; i += sampleSize) {

            short sample = (short) ((buffer[i + 1] << 8) | (buffer[i] & 0xFF));

            if (Math.abs(sample) > amplitudeThreshold) {
                return true;
            }
        }

        return false;
    }


}

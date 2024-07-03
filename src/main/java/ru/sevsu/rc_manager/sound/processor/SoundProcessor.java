package ru.sevsu.rc_manager.sound.processor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SoundProcessor {
    @Value("${sound.amplitude-threshold}")
    private double amplitudeThreshold;
    @Value("${sound.sample-size}")
    private int sampleSize;


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

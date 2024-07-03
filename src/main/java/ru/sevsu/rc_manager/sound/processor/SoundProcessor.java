package ru.sevsu.rc_manager.sound.processor;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Setter
public class SoundProcessor {
    @Value("${sound.amplitude-threshold}")
    private double amplitudeThreshold;
    @Value("${sound.sample-size-bytes}")
    private int sampleSizeBytes;


    public boolean isSound(byte[] buffer) {

        for (int i = 0; i < buffer.length; i += sampleSizeBytes) {
            // there takes to bytes of sound and combines to sample(lower byte must be unsigned)
            short sample = (short) ((buffer[i + 1] << 8) | (buffer[i] & 0xFF));
            // Check sample amplitude to define sound it or not
            if (Math.abs(sample) >= amplitudeThreshold) {
                return true;
            }
        }

        return false;
    }


}

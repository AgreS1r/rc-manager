package ru.sevsu.rc_manager.sound.processor;

import lombok.Setter;
import org.springframework.stereotype.Component;

import static java.lang.Math.abs;


@Component
@Setter
public class SoundProcessor {

    private int avgNoiseAmplitude;

    public int calculateAvgSoundAmplitude(byte[] buffer) {
        int sum = 0;
        for (byte b : buffer) {
            sum += abs(b);
        }

        return sum / buffer.length;

    }

    public boolean isSound(byte[] buffer) {
        int sum = 0;
        for (byte b : buffer) {
            sum += abs(b);
        }
        // if avg amplitude more than 1.5 * avg noise amplitude(calculated on calibration)
        // we can think that it is some command
        return sum / buffer.length >= avgNoiseAmplitude * 1.5;
    }


}

package ru.sevsu.rc_manager.sound.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundProcessorTest {

    private SoundProcessor soundProcessor;

    @BeforeEach
    void setUp() {
        soundProcessor = new SoundProcessor();
        soundProcessor.setAvgNoiseAmplitude(1);
    }

    @Test
    void testIsSoundWithNoSound() {
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        assertFalse(soundProcessor.isSound(buffer));
    }

    @Test
    void testIsSoundWithSound() {
        byte[] buffer = new byte[]{5, -5, 10, 12, 3, 8, 5, 3};
        assertTrue(soundProcessor.isSound(buffer));
    }

    @Test
    void testIsSoundWithAvgNoiseAmplitude() {
        byte[] buffer = new byte[]{2, 1, 2, 1};
        assertTrue(soundProcessor.isSound(buffer));
    }

    @Test
    void testIsSoundBellowAvgNoiseAmplitude() {
        byte[] buffer = new byte[]{1, 1, 1, 1};
        assertFalse(soundProcessor.isSound(buffer));
    }
}

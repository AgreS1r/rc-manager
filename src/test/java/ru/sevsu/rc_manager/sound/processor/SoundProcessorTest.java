package ru.sevsu.rc_manager.sound.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundProcessorTest {

    private SoundProcessor soundProcessor;

    @BeforeEach
    void setUp() {
        soundProcessor = new SoundProcessor();
        soundProcessor.setAmplitudeThreshold(1000);
        soundProcessor.setSampleSize(2);
    }

    @Test
    void testIsSoundWithNoSound() {
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        assertFalse(soundProcessor.isSound(buffer));
    }

    @Test
    void testIsSoundWithSound() {
        byte[] buffer = new byte[]{0, 0, 0, 0, (byte)0xF8, 0x03, (byte)0xF8, 0};
        assertTrue(soundProcessor.isSound(buffer));
    }

    @Test
    void testIsSoundWithThresholdEdge() {
        byte[] buffer = new byte[]{(byte)0xE8, 0x03, 0, 0};
        assertTrue(soundProcessor.isSound(buffer));
    }

    @Test
    void testIsSoundBellowThresholdEdge() {
        byte[] buffer = new byte[]{(byte)0xE7, 0x03, 0, 0};
        assertFalse(soundProcessor.isSound(buffer));
    }
}

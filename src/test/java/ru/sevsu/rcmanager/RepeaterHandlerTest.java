package ru.sevsu.rcmanager;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sevsu.rcmanager.sound.handler.RepeaterHandler;
import ru.sevsu.rcmanager.sound.processor.SoundConverter;
import ru.sevsu.rcmanager.sound.processor.SoundFormat;


import javax.sound.sampled.AudioFormat;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class RepeaterHandlerTest {

    private  RepeaterHandler repeaterHandler;

    public RepeaterHandler createHandler(AudioFormat audioFormat){
      return repeaterHandler = new RepeaterHandler(new SoundConverter(
              new SoundFormat(audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(),
                      audioFormat.getChannels(), true, audioFormat.isBigEndian())
        ));
    }


    @Test
    public void testGenerateBeepBytes_SampleRate44100Hz() {
        // Arrange
        AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, false);

        repeaterHandler = createHandler(audioFormat);
        // Act
        byte[] beepBytes = repeaterHandler.generateBeepBytes(audioFormat);

        // Assert
        assertNotNull(beepBytes);
        assertEquals(44100 * 500 / 1000 * 2, beepBytes.length);
    }

    @Test
    public void testGenerateBeepBytes_SampleRate48000Hz() {
        // Arrange
        AudioFormat audioFormat = new AudioFormat(48000, 16, 1, true, false);
        repeaterHandler = createHandler(audioFormat);

        // Act
        byte[] beepBytes = repeaterHandler.generateBeepBytes(audioFormat);

        // Assert
        assertNotNull(beepBytes);
        assertEquals(48000 * 500 / 1000 * 2, beepBytes.length);
    }

    @Test
    public void testGenerateBeepBytes_InvalidAudioFormat() {
        // Arrange
        AudioFormat audioFormat = null;

        // Act and Assert
        try {
            repeaterHandler = createHandler(audioFormat);
            repeaterHandler.generateBeepBytes(audioFormat);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}
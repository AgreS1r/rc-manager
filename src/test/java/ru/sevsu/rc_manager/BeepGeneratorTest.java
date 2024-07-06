import org.junit.Test;
import static org.junit.Assert.*;

public class BeepGeneratorTest {

    @Test
    public void testGenerateBeepBytes_SampleRate44100Hz() {
        // Arrange
        AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, false);
        BeepGenerator beepGenerator = new BeepGenerator();

        // Act
        byte[] beepBytes = beepGenerator.generateBeepBytes(audioFormat);

        // Assert
        assertNotNull(beepBytes);
        assertEquals(44100 * 500 / 1000 * 2, beepBytes.length);
    }

    @Test
    public void testGenerateBeepBytes_SampleRate48000Hz() {
        // Arrange
        AudioFormat audioFormat = new AudioFormat(48000, 16, 1, true, false);
        BeepGenerator beepGenerator = new BeepGenerator();

        // Act
        byte[] beepBytes = beepGenerator.generateBeepBytes(audioFormat);

        // Assert
        assertNotNull(beepBytes);
        assertEquals(48000 * 500 / 1000 * 2, beepBytes.length);
    }

    @Test
    public void testGenerateBeepBytes_InvalidAudioFormat() {
        // Arrange
        AudioFormat audioFormat = null;
        BeepGenerator beepGenerator = new BeepGenerator();

        // Act and Assert
        try {
            beepGenerator.generateBeepBytes(audioFormat);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}
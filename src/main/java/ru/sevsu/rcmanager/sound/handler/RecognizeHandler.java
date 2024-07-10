package ru.sevsu.rcmanager.sound.handler;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vosk.Model;
import org.vosk.Recognizer;
import ru.sevsu.rcmanager.sound.processor.SoundConverter;

// Handler transcripts sound to text, if it possible
@Component
@Slf4j
@RequiredArgsConstructor
public class RecognizeHandler implements Handler {
    private Recognizer recognizer;
    private ObjectMapper objectMapper;
    private final SoundConverter soundConverter;
    @Value("${sound.recognize.model-path}")
    private String modelPath;
    @Value("${sound.sample-rate}")
    private int simpleRate;

    private static final String TEMP_FILE_NAME = "RecognizeTemp.waw";
    private static final String RECOGNIZED_FILE_NAME = "recognized.txt";
    private File textRecognizeFile;

    @PostConstruct
    private void init() throws IOException {
        Model model = new Model(modelPath);
        recognizer = new Recognizer(model, simpleRate);
        objectMapper = new ObjectMapper();
        log.info("RecognizeHandler init!");
        textRecognizeFile = new File(RECOGNIZED_FILE_NAME);
    }


    @Override
    public void handle(byte[] sound) {
        try {
            File file = new File(TEMP_FILE_NAME);
            FileWriter writer = new FileWriter(textRecognizeFile);
            try {
                AudioInputStream audioInputStream = soundConverter.byteToStream(sound);
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
                        file);
            } catch (IOException e) {
                log.warn("Error saving audio file: {}", e.getMessage());
            }
            InputStream ais = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(new FileInputStream(TEMP_FILE_NAME)));
            int nbytes;
            byte[] b = new byte[1024];
            while ((nbytes = ais.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    JsonNode jsonNode = objectMapper.readTree(recognizer.getResult());
                    String text = jsonNode.get("text").asText();
                    writer.append(text);
                    log.info(text);
                }
            }
            file.delete();
            writer.close();
        } catch (IOException | UnsupportedAudioFileException e) {
            log.warn(e.getMessage());
        }
    }
}

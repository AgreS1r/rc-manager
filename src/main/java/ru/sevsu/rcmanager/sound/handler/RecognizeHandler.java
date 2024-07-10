package ru.sevsu.rcmanager.sound.handler;

import javax.sound.sampled.AudioInputStream;
import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
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

    @PostConstruct
    private void init() throws IOException {
        log.debug("Initializing RecognizeHandler...");
        Model model = new Model(modelPath);
        recognizer = new Recognizer(model, simpleRate);
        objectMapper = new ObjectMapper();
        log.info("RecognizeHandler init!");
    }


    @Override
    public void handle(byte[] sound) {
        try {
            log.debug("Handling sound byte array...");
            LibVosk.setLogLevel(LogLevel.WARNINGS);

            AudioInputStream ais = soundConverter.byteToStream(sound);
            log.debug("Converted sound to AudioInputStream...");
            int nbytes;
            byte[] b = new byte[4096];
            while ((nbytes = ais.read(b)) >= 0) {
                log.debug("Reading audio data...");
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    log.debug("Recognized speech, processing result...");
                    JsonNode jsonNode = objectMapper.readTree(recognizer.getResult());
                    String text = jsonNode.get("text").asText();
                    log.info(text);
                } else {
                    log.warn("Failed to recognize speech");
                }
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
}

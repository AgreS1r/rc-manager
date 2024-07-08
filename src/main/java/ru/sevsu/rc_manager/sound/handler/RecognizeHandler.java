package ru.sevsu.rc_manager.sound.handler;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;
import ru.sevsu.rc_manager.sound.processor.SoundConverter;


@Component
@Slf4j
@RequiredArgsConstructor
public class RecognizeHandler implements Handler {
    private Recognizer recognizer;
    private ObjectMapper objectMapper;
    private final SoundConverter soundConverter;

    @PostConstruct
    private void init() throws IOException {
        String modelPath = "/home/user2/Documents/vosk-model-small-ru-0.22";//ссылка выносится в конфиг
        Model model = new Model(modelPath);
        recognizer = new Recognizer(model, 16000);
        objectMapper = new ObjectMapper();
    }


    @Override
    public void handle(byte[] sound) {
        try {
            LibVosk.setLogLevel(LogLevel.WARNINGS);
            String audioFilePath = "temp.wav";

            AudioInputStream ais = soundConverter.byteToStream(sound);
                int nbytes;
                byte[] b = new byte[4096];
                while ((nbytes = ais.read(b)) >= 0){
                    if (recognizer.acceptWaveForm(b, nbytes)) {
                        JsonNode jsonNode = objectMapper.readTree(recognizer.getResult());
                        String text = jsonNode.get("text").asText();
                        log.info(text);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

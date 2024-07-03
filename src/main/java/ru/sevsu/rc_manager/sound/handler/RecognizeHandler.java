package ru.sevsu.rc_manager.sound.handler;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.time.LocalDateTime;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

@Component
@Slf4j
public class RecognizeHandler implements Handler {
    private Recognizer recognizer;

    @PostConstruct
    private void init() throws IOException {
        String modelPath = "C:\\Users\\user\\Downloads\\vosk-model-small-ru-0.22";
        Model model = new Model(modelPath);
        recognizer = new Recognizer(model, 16000);
    }

    @Override
    public void handle(AudioInputStream audioInputStream) {
        try {
            LibVosk.setLogLevel(LogLevel.DEBUG);

            // Assuming the path to the model and audio file are known and accessible
            String audioFilePath = "temp.wav";
            /*try {
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
                        new File(audioFilePath));
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
*/

            InputStream ais = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(new FileInputStream(audioFilePath)));
                int nbytes;
                byte[] b = new byte[4096];
                while ((nbytes = ais.read(b)) >= 0){
                    if (recognizer.acceptWaveForm(b, nbytes)) {
                        System.out.println(recognizer.getResult());
                    }
                }
                System.out.println(recognizer.getFinalResult());
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }
    }

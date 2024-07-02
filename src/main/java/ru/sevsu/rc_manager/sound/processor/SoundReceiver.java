package ru.sevsu.rc_manager.sound.processor;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sevsu.rc_manager.sound.handler.SoundHandler;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

@Service
public class SoundReceiver {
    private final SoundProcessor soundProcessor;
    private final SoundHandler soundHandler;
    private final SoundFormat soundFormat;
    private static final Logger log = LoggerFactory.getLogger(SoundReceiver.class);
    private final int minDuration;

    @Autowired
    public SoundReceiver(SoundProcessor soundProcessor, SoundHandler soundHandler, SoundFormat soundFormat,
                         @Value("${sound.min-duration}") int minDuration) {
        this.soundProcessor = soundProcessor;
        this.soundHandler = soundHandler;
        this.soundFormat = soundFormat;
        this.minDuration = minDuration;
    }

    @PostConstruct
    public void processAudio() {
        try {
            AudioFormat format = soundFormat.getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();


            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            long startTime = 0;
            boolean recording = false;

            while (true) {
                byte[] data = new byte[16000];
                int numBytesRead = line.read(data, 0, data.length);
                if (soundProcessor.isSound(data)) {
                    if (!recording) {
                        startTime = System.currentTimeMillis();
                        recording = true;
                    }

                    buffer.write(data, 0, numBytesRead);
                } else {
                    if (recording) {
                        long endTime = System.currentTimeMillis();
                        long duration = endTime - startTime;
                        if (duration > minDuration) {
                            soundHandler.handle(buffer.toByteArray());
                        }

                        buffer.reset();
                        recording = false;
                    }
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

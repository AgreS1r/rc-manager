package ru.sevsu.rc_manager.sound.processor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sevsu.rc_manager.sound.handler.SoundHandler;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SoundReceiver {
    private final SoundProcessor soundProcessor;
    private final SoundHandler soundHandler;
    private final SoundFormat soundFormat;

    @Value("${sound.min-duration}")
    private int minDuration;
    @Value("${sound.calibration-time-ms}")
    private int calibrationTimeMs;

    @PostConstruct
    public void receiveSound() {
        try {
            AudioFormat format = soundFormat.getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();


            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            long startTime;
            long calibrateDuration = 0;
            int sum = 0;
            int count = 0;
            startTime = System.currentTimeMillis();
            while (calibrateDuration < calibrationTimeMs) {
                byte[] data = new byte[16000];
                line.read(data, 0, data.length);
                sum = sum + soundProcessor.calculateAvgSoundAmplitude(data);
                count++;
                calibrateDuration = System.currentTimeMillis() - startTime;
            }
            soundProcessor.setAvgNoiseAmplitude(sum / count);
            log.info("Калибровка закончена");


            startTime = 0;
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
                        } else {
                            log.debug("Duration less than min");
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

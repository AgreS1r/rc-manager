package ru.sevsu.rcmanager.sound.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.sevsu.rcmanager.sound.processor.SoundConverter;
import ru.sevsu.rcmanager.sound.processor.SoundFormat;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Component
@ConditionalOnProperty(value = "handler.repeater", havingValue = "true")
@Slf4j
@RequiredArgsConstructor

public class RepeaterHandler implements Handler {

    private final SoundConverter soundConverter;

    @Override
    public void handle(byte[] sound) {
        // Получаем метаданные из AudioInputStream
        AudioInputStream audioInputStream = soundConverter.byteToStream(sound);
        AudioFormat audioFormat = audioInputStream.getFormat();
        int frameSize = audioFormat.getFrameSize();

        // Читаем аудиоданные из AudioInputStream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[frameSize];
        int bytesRead;
        try {
            while ((bytesRead = audioInputStream.read(buffer, 0, frameSize)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.warn("Error reading audio input stream: " + e.getMessage());
        }

        // Получаем массив байтов из ByteArrayOutputStream
        byte[] soundBytes = bos.toByteArray();

        // Добавляем писк на полсекунды перед воспроизведением звука
        byte[] beepBytes = generateBeepBytes(audioFormat);
        byte[] soundBytesWithBeep = new byte[soundBytes.length + beepBytes.length];
        System.arraycopy(beepBytes, 0, soundBytesWithBeep, 0, beepBytes.length);
        System.arraycopy(soundBytes, 0, soundBytesWithBeep, beepBytes.length, soundBytes.length);

        // Повторяем проигрывание звука
        repeatSound(soundBytesWithBeep, audioFormat);
    }

    private void repeatSound(byte[] soundBytes, AudioFormat audioFormat) {
        try {
            AudioInputStream repeatedAudioInputStream = soundConverter.byteToStream(soundBytes);
            Clip clip = AudioSystem.getClip();
            clip.open(repeatedAudioInputStream);
            clip.setFramePosition(0);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
            clip.stop();
            clip.close(); //
        } catch (IOException | LineUnavailableException | InterruptedException e) {
            log.warn(e.getMessage());
        }
    }

    public byte[] generateBeepBytes(AudioFormat audioFormat) {
        // Генерируем писк на полсекунды
        int beepDuration = 500; // 500 ms
        int beepFrequency = 1000; // 1000 Hz
        int sampleRate = (int) audioFormat.getSampleRate();
        int numSamples = (int) (beepDuration / 1000.0 * sampleRate);
        byte[] beepBytes = new byte[numSamples * audioFormat.getFrameSize()];
        for (int i = 0; i < numSamples; i++) {
            short sample = (short) (Math.sin(2 * Math.PI * beepFrequency * i / sampleRate) * Short.MAX_VALUE);
            byte[] sampleBytes = new byte[2];
            sampleBytes[0] = (byte) (sample & 0xFF);
            sampleBytes[1] = (byte) ((sample >> 8) & 0xFF);
            System.arraycopy(sampleBytes, 0, beepBytes, i * audioFormat.getFrameSize(), audioFormat.getFrameSize());
        }
        return beepBytes;
    }
}
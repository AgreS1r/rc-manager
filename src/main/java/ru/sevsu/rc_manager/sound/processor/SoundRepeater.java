package ru.sevsu.rc_manager.sound.handler;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class RepeaterHandler implements Handler {
    private static final Logger LOGGER = Logger.getLogger(RepeaterHandler.class.getName());

    @Override
    public void handle(AudioInputStream audioInputStream) {
        // Получаем метаданные из AudioInputStream
        AudioFormat audioFormat = audioInputStream.getFormat();
        int frameSize = audioFormat.getFrameSize();
        int frameRate = (int) audioFormat.getFrameRate();

        // Читаем аудиоданные из AudioInputStream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[frameSize];
        int bytesRead;
        try {
            while ((bytesRead = audioInputStream.read(buffer, 0, frameSize)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            LOGGER.severe("Error reading audio input stream: " + e.getMessage());
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
        // Создаем новый AudioInputStream из массива байтов
        ByteArrayInputStream bais = new ByteArrayInputStream(soundBytes);
        AudioInputStream repeatedAudioInputStream = new AudioInputStream(bais, audioFormat, soundBytes.length / audioFormat.getFrameSize());
    }

    private byte[] generateBeepBytes(AudioFormat audioFormat) {
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
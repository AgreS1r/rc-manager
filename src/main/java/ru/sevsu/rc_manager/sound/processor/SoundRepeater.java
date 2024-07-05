package ru.sevsu.rc_manager.sound.handler;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Обработчик получает массив байтов и метаданные файла wav
public class RepeaterHandler implements Handler {
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
            // Обработка исключения
        }

        // Получаем массив байтов из ByteArrayOutputStream
        byte[] soundBytes = bos.toByteArray();

        // Повторяем проигрывание звука
        repeatSound(soundBytes, audioFormat);
    }

    private void repeatSound(byte[] soundBytes, AudioFormat audioFormat) {
        // Создаем новый AudioInputStream из массива байтов
        ByteArrayInputStream bais = new ByteArrayInputStream(soundBytes);
        AudioInputStream repeatedAudioInputStream = new AudioInputStream(bais, audioFormat, soundBytes.length / audioFormat.getFrameSize());

        // Отправляем повторенный звук другим обработчикам
        // Например, отправляем его в TelegramSendHandler
        TelegramSendHandler telegramSendHandler = new TelegramSendHandler();
        telegramSendHandler.handle(repeatedAudioInputStream);
    }
}
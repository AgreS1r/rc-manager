package ru.sevsu.rcmanager.sound.handler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.sevsu.rcmanager.sound.handler.bot.TelegramBot;
import ru.sevsu.rcmanager.sound.processor.SoundConverter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
// Handler sends sound in telegram chat
public class TelegramSendHandler implements Handler {
    @Value("${sound.save-path}")
    private String savePath;
    private final TelegramBot bot;
    private final SoundConverter soundConverter;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss");



    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error creating bot telegrams in TelegramSendHandler");
        }
        log.info("TelegramSendHandler init!");

    }

    @Override
    public void handle(byte[] sound) {
        String dateTimePath = LocalDateTime.now().format(formatter) + "_signal.wav";

        File file = new File(dateTimePath);
        try {
            AudioInputStream audioInputStream = soundConverter.byteToStream(sound);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
                    file);
        } catch (IOException e) {
            log.warn("Error saving audio file: {}", e.getMessage());
        }
        bot.sendSound(file);
        file.delete();
    }
}
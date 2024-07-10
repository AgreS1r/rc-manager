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

import java.io.File;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
// Handler sends sound in telegram chat
public class TelegramSendHandler implements Handler {
    @Value("${sound.save-path}")
    private String savePath;
    private final TelegramBot bot;

    @PostConstruct
    private void init() {
        log.debug("Initializing TelegramSendHandler...");
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

        log.debug("Handling sound byte array...");
        File dir = new File(savePath);
        log.debug("Getting list of files in directory...");
        LinkedList<File> files = new LinkedList<>(Arrays.asList(dir.listFiles()));
        log.debug("Sorting files by last modified date...");
        files.sort(Comparator.comparingLong(File::lastModified));
        log.debug("Sending last modified file to Telegram...");
        bot.sendSound(files.getLast());
    }
}
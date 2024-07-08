package ru.sevsu.rc_manager.sound.handler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.sevsu.rc_manager.sound.handler.bot.TelegramBot;
import javax.sound.sampled.AudioInputStream;
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
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e){
            log.error("Error creating bot telegrams in TelegramSendHandler");
        }
    }
    @Override
    public void handle(byte[] sound) {

        File dir = new File(savePath);
        LinkedList<File> files = new LinkedList<>(Arrays.asList(dir.listFiles()));
        files.sort(Comparator.comparingLong(File::lastModified));
        bot.sendSound(files.getLast());
    }
}
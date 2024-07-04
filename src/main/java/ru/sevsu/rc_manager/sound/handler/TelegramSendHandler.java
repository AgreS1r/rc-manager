package ru.sevsu.rc_manager.sound.handler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final TelegramBot bot;
    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    @Override
    public void handle(AudioInputStream audioInputStream) {

        File dir = new File("C:\\Users\\user\\IdeaProjects\\rc-manager2");
        LinkedList<File> files = new LinkedList<>(Arrays.asList(dir.listFiles()));
        files.sort(Comparator.comparingLong(File::lastModified));
        bot.sendSound(files.getLast());
    }
}
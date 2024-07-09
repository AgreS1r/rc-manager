package ru.sevsu.rc_manager.sound.handler.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.chat-id}")
    private String chatId;
    @Value("${telegram.bot-token}")
    private String botToken;
    private static final String INFO_TEXT = "Бот для отправки принятых аудиозаписей";

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        String ChatId = update.getMessage().getChatId().toString();
        if (text.equals("/info")) {
            SendMessage message = new SendMessage();
            message.setChatId(ChatId);
            message.setText(INFO_TEXT);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.warn(e.getMessage());
            }
        }
    }

    public void sendSound(File soundFile) {
        InputFile inputFile = new InputFile(soundFile);
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(inputFile);
        try {
            execute(sendDocumentRequest); // Отправляем файл
        } catch (TelegramApiException e) {
            log.warn(e.getMessage());
        }
        log.debug("Отправлен файл " + soundFile.getName() + " в чат " + chatId);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return "Manager_rc_Bot";
    }
}

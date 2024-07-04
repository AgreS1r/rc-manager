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

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

@Component
@Slf4j

public class TelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.chat-id}")
    private String idPath;
    @Value("${telegram.bot-token}")
    private String botToken;
    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        String ChatId = update.getMessage().getChatId().toString();
        System.out.println(ChatId);
        if (text.equals("/sendfile")) {

            File file = new File("C:\\Users\\user\\Desktop\\1.mp3");
            InputFile inputFile = new InputFile(file);
            SendDocument sendDocumentRequest = new SendDocument();
            sendDocumentRequest.setChatId(ChatId);
            sendDocumentRequest.setDocument(inputFile);
            try {
                execute(sendDocumentRequest); // Отправляем файл
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(ChatId);
            message.setText(text);
            try {
                this.execute(message);
            } catch (TelegramApiException e) {
                log.error("При ответе возникла ошибка");
            }
        }
    }

    public void sendSound(File soundFile) {
        InputFile inputFile = new InputFile(soundFile);
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(idPath);
        sendDocumentRequest.setDocument(inputFile);
        try {
            execute(sendDocumentRequest); // Отправляем файл
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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

package ru.sevsu.rc_manager.sound.handler;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.sound.sampled.AudioInputStream;

public interface Handler {
    void handle(AudioInputStream audioInputStream) throws TelegramApiException;
}

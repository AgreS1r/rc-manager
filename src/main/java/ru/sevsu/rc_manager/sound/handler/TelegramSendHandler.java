package ru.sevsu.rc_manager.sound.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioInputStream;

// Handler sends sound in telegram chat
@Component
@Slf4j
public class TelegramSendHandler implements Handler {
    @Override
    public void handle(byte[] sound) {
        log.info(TelegramSendHandler.class.toString() + "handled");
    }
}

package ru.sevsu.rc_manager.sound.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


// Handler transcripts sound to text, if it possible
@Component
@Slf4j
public class RecognizeHandler implements Handler {

    @Override
    public void handle(byte[] sound) {
        log.info(RecognizeHandler.class.toString() + "handled");
    }
}

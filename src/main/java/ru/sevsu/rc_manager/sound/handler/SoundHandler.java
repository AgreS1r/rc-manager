package ru.sevsu.rc_manager.sound.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sevsu.rc_manager.sound.processor.SoundConverter;

import javax.sound.sampled.AudioInputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoundHandler {
    private final SaveSoundHandler saveSoundHandler;
    private final SoundConverter soundConverter;
    private final TelegramSendHandler telegramSendHandler;

// This handler convert byte array to AudioStream and send it to other handlers
    public void handle(byte[] sound) {
        log.info("Audio handled");
        AudioInputStream audioInputStream = soundConverter.byteToStream(sound);
        saveSoundHandler.handle(audioInputStream);
        telegramSendHandler.handle(audioInputStream);
    }
}

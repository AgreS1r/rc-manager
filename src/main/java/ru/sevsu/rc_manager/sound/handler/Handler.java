package ru.sevsu.rc_manager.sound.handler;

import javax.sound.sampled.AudioInputStream;

public interface Handler {
    void handle(byte[] sound);
}

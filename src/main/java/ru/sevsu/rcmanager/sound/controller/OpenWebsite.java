package ru.sevsu.rcmanager.sound.controller;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class OpenWebsite {
    public static void openWebsite(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

        try {
            if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
                // Список браузеров для попытки открытия
                String[] browsers = {"firefox", "mozilla", "chromium", "google-chrome", "opera", "links", "elinks", "lynx"};

                // Строим команду для открытия URL в первом доступном браузере
                StringBuilder cmd = new StringBuilder();
                for (int i = 0; i < browsers.length; i++) {
                    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " '" + url + "'");
                }

                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            } else {
                System.out.println("Операционная система не поддерживается.");
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
}

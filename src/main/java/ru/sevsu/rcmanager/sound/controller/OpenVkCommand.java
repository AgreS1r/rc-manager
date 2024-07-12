package ru.sevsu.rcmanager.sound.controller;

public class OpenVkCommand implements TaskController.Command {
    @Override
    public void execute() {
        System.out.println("Открываю ВКонтакте... Дуров верни стену!");
        OpenWebsite.openWebsite("https://www.vk.com");
    }
}
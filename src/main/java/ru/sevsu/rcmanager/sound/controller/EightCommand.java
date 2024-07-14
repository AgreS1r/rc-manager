package ru.sevsu.rcmanager.sound.controller;

public class EightCommand implements TaskController.Command {
    @Override
    public void execute() {
        System.out.println("Выполнение команды восемь...");
        OpenWebsite.openWebsite("https://www.google.com");
    }
}
package ru.sevsu.rcmanager.sound.controller;

public class OpenDoorsCommand implements TaskController.Command {
    @Override
    public void execute() {
        System.out.println("Открытие ворот...");

    }
}
package ru.sevsu.rcmanager.sound.controller;

import java.util.HashMap;
import java.util.Map;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskController {
    private Map<String, Command> commandsMap = new HashMap<>();
    private final String[] commands = {"открой ворота","верни стену", "большой брат следит за тобой"};
    public boolean findString(String[] strings, String desired) {
        for (String str : strings) {
            if (desired.contains(str)) {
                return true;
            }
        }
        return false; // Если до этого момента совпадение не найдено, возвращаем false.
    }

    public interface Command {
        void execute();
    }

    public TaskController() {
        // Инициализация команд
        commandsMap.put("открой ворота", new OpenDoorsCommand());
        commandsMap.put("большой брат следит за тобой", new EightCommand());
        commandsMap.put("верни стену", new OpenVkCommand());
    }

    public void executeCommand(String commandName) {
        if (commandsMap.containsKey(commandName)) {
            Command command = commandsMap.get(commandName);
            command.execute();
            log.info("Команда '{}' выполнена.", commandName);
        } else {
            log.debug("Команда '{}' не найдена.", commandName);
        }
    }

    public void searchCommand(String command){
        if ((command == null) || (command.isBlank())){
            return;
        }
        command = command.toLowerCase();

        if (findString(commands, command)){
            executeCommand(command);
        }
    }
}

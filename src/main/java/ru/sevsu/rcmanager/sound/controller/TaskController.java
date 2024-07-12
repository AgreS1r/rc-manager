package ru.sevsu.rcmanager.sound.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskController {
    private int levenshteinDistance;
    private Map<String, Command> commandsMap = new HashMap<>();
    private final String[] commands = {"открой ворота","дуров верни стену", "большой брат следит за тобой"};

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

    public TaskController(@Value("${sound.levenshtein-distance}") int levenshteinDistance) {
        this.levenshteinDistance = levenshteinDistance;
        // Инициализация команд
        commandsMap.put("открой ворота", new OpenDoorsCommand());
        commandsMap.put("большой брат следит за тобой", new EightCommand());
        commandsMap.put("дуров верни стену", new OpenVkCommand());
    }

    public void executeCommand(String commandName) {
            Command command = commandsMap.get(commandName);
            command.execute();
            log.info("Команда '{}' выполнена.", commandName);
    }

    public void searchCommand(String command){
        if ((command == null) || (command.isBlank())) {
            return;
        }
        command = command.toLowerCase();

        for (String key : commands) {
            LevenshteinDistance distance = new LevenshteinDistance();
            int diff = distance.apply(command, key);

            // Проверяем, не превышает ли разница в 2 символа
            if (diff <= levenshteinDistance) {
                executeCommand(key);
                return; // Выходим из метода после выполнения команды
            }
        }

        log.debug("Команда '{}' не найдена или не соответствует ни одной из команд.", command);
    }
}
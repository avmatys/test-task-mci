package com.task.mci.command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    
    private final Map<String, Command> commands = new HashMap<>();

    public void register(String name, Command cmd) {
        commands.put(name.toLowerCase(), cmd);
    }

    public Command get(String name) {
        return commands.get(name.toLowerCase());
    }

    public Map<String, Command> all() {
        return commands;
    }

}


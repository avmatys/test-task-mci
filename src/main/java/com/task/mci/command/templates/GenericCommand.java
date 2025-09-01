package com.task.mci.command.templates;

import java.io.IOException;

import com.task.mci.command.Command;
import com.task.mci.command.CommandHandler;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;

public class GenericCommand implements Command {

    private final String description;
    private final CommandHandler handler;

    public GenericCommand(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    @Override
    public boolean execute(String[] args, InputSource in, OutputTarget out) throws IOException {
        return handler.handle(args, in, out);
    }

    @Override
    public String description() {
        return description;
    }

}

package com.task.mci.command;

import java.io.IOException;

import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;

@FunctionalInterface
public interface CommandHandler {
    boolean handle(String[] args, InputSource in, OutputTarget out) throws IOException;
}

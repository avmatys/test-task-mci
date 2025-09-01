package com.task.mci.command;

import java.io.IOException;

import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;

public interface Command {
    boolean execute(String[] args, InputSource in, OutputTarget out) throws IOException;
    String description();
}




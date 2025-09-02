package com.task.mci.io.impl;

import java.io.IOException;

import com.task.mci.io.OutputTarget;

public class ConsoleOutputTarget implements OutputTarget {

    @Override
    public void write(String text) throws IOException {
        System.out.print(text);
    }

    @Override
    public void close() throws IOException {}

}

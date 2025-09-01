package com.task.mci.io;

import java.io.IOException;

public class ConsoleOutputTarget implements OutputTarget {

    @Override
    public void write(String text) throws IOException {
        System.out.print(text);
    }

    @Override
    public void close() throws IOException {}

}

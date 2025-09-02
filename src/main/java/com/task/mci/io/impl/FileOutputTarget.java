package com.task.mci.io.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.task.mci.io.OutputTarget;

public class FileOutputTarget implements OutputTarget {

    private final PrintWriter writer;

    public FileOutputTarget(String filePath) throws IOException {
        this.writer = new PrintWriter(new FileWriter(filePath, true), true);
    }

    @Override
    public void write(String text) throws IOException {
        writer.print(text);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
    
}


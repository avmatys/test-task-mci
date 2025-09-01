package com.task.mci.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileInputSource implements InputSource {
    
    private final BufferedReader reader;

    public FileInputSource(String path) throws IOException {
        this.reader = new BufferedReader(new FileReader(path));
    }

    @Override
    public String readLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}


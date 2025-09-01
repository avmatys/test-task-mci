package com.task.mci.io;

import java.io.IOException;

public interface OutputTarget {
    void write(String text) throws IOException;
    void close() throws IOException;
}

package com.task.mci.io;

import java.io.IOException;

public interface InputSource {
    String readLine() throws IOException;
    void close() throws IOException;
}


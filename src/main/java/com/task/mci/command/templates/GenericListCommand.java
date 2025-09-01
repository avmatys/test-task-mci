package com.task.mci.command.templates;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Function;

import com.task.mci.command.Command;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;
import com.task.mci.service.GenericService;

public class GenericListCommand<T, M> implements Command {

    private final GenericService<T, M> service;
    private final Function<T, String> formatter;
    private final String description;

    public GenericListCommand(GenericService<T, M> service,
                              Function<T, String> formatter,
                              String description) {
        this.service = service;
        this.formatter = formatter;
        this.description = description;
    }

    @Override
    public boolean execute(String[] args, InputSource in, OutputTarget out) throws IOException {
        try {
            for (T item : service.findAll()) {
                out.write(formatter.apply(item) + "\n");
            }
            out.write("\n");
        } catch (SQLException e) {
            out.write("Service error: " + e.getMessage() + "\n");
        }
        return true;
    }

    @Override
    public String description() {
        return description;
    }
}

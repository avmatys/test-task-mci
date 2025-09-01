package com.task.mci.command.templates;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Function;

import com.task.mci.command.Command;
import com.task.mci.dao.CrudDao;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;

public class GenericListCommand<T> implements Command {

    private final CrudDao<T, Integer> dao;
    private final Function<T, String> formatter;
    private final String description;

    public GenericListCommand(CrudDao<T, Integer> dao,
                              Function<T, String> formatter,
                              String description) {
        this.dao = dao;
        this.formatter = formatter;
        this.description = description;
    }

    @Override
    public boolean execute(String[] args, InputSource in, OutputTarget out) throws IOException {
        try {
            for (T item : dao.findAll()) {
                out.write(formatter.apply(item) + "\n");
            }
            out.write("\n");
        } catch (SQLException e) {
            out.write("Database error: " + e.getMessage() + "\n");
        }
        return true;
    }

    @Override
    public String description() {
        return description;
    }

}

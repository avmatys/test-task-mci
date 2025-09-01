package com.task.mci.command.templates;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;
import java.util.function.Function;

import com.task.mci.command.Command;
import com.task.mci.command.util.CommandArgumentParser;
import com.task.mci.dao.CrudDao;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;

public class GenericAddCommand<T> implements Command {

    private final CrudDao<T, Integer> dao;
    private final ParamSpec[] specs;
    private final Function<Map<String,String>,T> builder;
    private final Function<T,String> formatter;
    private final String description;

    public GenericAddCommand(CrudDao<T, Integer> dao,
                             ParamSpec[] specs,
                             Function<Map<String,String>,T> builder,
                             Function<T,String> formatter,
                             String description) {
        this.dao = dao;
        this.specs = specs;
        this.builder = builder;
        this.formatter = formatter;
        this.description = description;
    }

    @Override
    public boolean execute(String[] args, InputSource in, OutputTarget out) throws IOException {
        Map<String, String> params;
        try {
            params = CommandArgumentParser.parse(args);
        } catch (ParseException e) {
            out.write("Error parsing arguments: " + e.getMessage() + "\n");
            return true;
        }
        boolean interactive = params.containsKey("-i");
        for (ParamSpec spec : specs) {
            if (interactive && !params.containsKey(spec.key())) {
                out.write(spec.prompt() + ": ");
                params.put(spec.key(), in.readLine());
            }
        }
        for (ParamSpec spec : specs) {
            if (spec.required() && (params.get(spec.key()) == null || params.get(spec.key()).isBlank())) {
                out.write("Parameter " + spec.key() + " is required\n");
                return true;
            }
        }
        try {
            T entity = builder.apply(params);
            T created = dao.insert(entity);
            out.write(formatter.apply(created) + "\n");
        } catch (NumberFormatException e) {
            out.write("Invalid numeric value: " + e.getMessage() + "\n");
        } catch (SQLException ex) {
            out.write("Database error: " + ex.getMessage() + "\n");
        }
        return true;
    }

    @Override
    public String description() {
        return description;
    }

}

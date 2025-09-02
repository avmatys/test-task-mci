package com.task.mci.command.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;
import java.util.function.Function;

import com.task.mci.command.Command;
import com.task.mci.command.util.CommandArgumentParser;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;
import com.task.mci.service.GenericService;
import com.task.mci.service.validation.ValidationException;

public class GenericAddCommand<T, M> implements Command {

    private final GenericService<T, M> service;
    private final ParamSpec[] specs;
    private final Function<Map<String,String>, T> builder;
    private final Function<T,String> formatter;
    private final String description;

    public GenericAddCommand(GenericService<T, M> service,
                             ParamSpec[] specs,
                             Function<Map<String,String>,T> builder,
                             Function<T,String> formatter,
                             String description) {
        this.service = service;
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
        // Check if interactive mode is needed
        boolean interactive = params.containsKey("-i");
        for (ParamSpec spec : specs) {
            if (interactive && !params.containsKey(spec.key())) {
                out.write(spec.prompt() + ": ");
                params.put(spec.key(), in.readLine());
            }
        }
        // Validate required parameters
        for (ParamSpec spec : specs) {
            String value = params.get(spec.key());
            if (spec.requiredIf().test(params) && (value == null || value.isBlank())) {
                out.write("Parameter " + spec.key() + " is required\n");
                return true;
            }
            if (value != null && !value.isBlank() && !spec.validator().test(value)) {
                out.write("Parameter " + spec.key() + " is invalid: " + spec.errorMessage() + "\n");
                return true;
            }
        }
        // Build entity and handle possible exceptions
        try {
            T entity  = builder.apply(params);
            T created = service.create(entity);
            out.write(formatter.apply(created) + "\n\n");
        } catch (NumberFormatException e) {
            out.write("Invalid numeric value: " + e.getMessage() + "\n");
        } catch (ValidationException ve) {
            for (String err : ve.getErrors()) {
                out.write("Validation error: " + err + "\n");
            }
        } catch (SQLException e) {
            out.write("Database error: " + e.getMessage() + "\n");
        }
        catch (IOException e) {
            out.write("I/O error: " + e.getMessage() + "\n");
        } catch (Exception ex) {
            out.write("Unexpected error: " + ex.getClass().getSimpleName()+ " – " + ex.getMessage() + "\n");
        }
        return true;
    }

    @Override
    public String description() {
        return description;
    }
}

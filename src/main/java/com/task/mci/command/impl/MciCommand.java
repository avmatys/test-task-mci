package com.task.mci.command.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import com.task.mci.command.Command;
import com.task.mci.command.util.CommandArgumentParser;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;    
import com.task.mci.service.MciService;    

public class MciCommand implements Command {

    private final MciService service;
    private final String description;

    public MciCommand(MciService service, String description) {
        this.service = service;
        this.description = description;
    }

    @Override
    public String description() {
        return description;
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
        if (interactive && !params.containsKey("-stageId")) {
            out.write("Shipment Stage ID: ");
            params.put("-stageId", in.readLine());
        }
        String value = params.get("-stageId");
        if (value == null || value.isBlank()) {
            out.write("Parameter -stageId is required\n");
            return true;
        }
        try {
            int stageId = Integer.parseInt(params.get("-stageId"));
            var items = service.findMci(stageId);
            if (items != null) {
                for (var x : items) {
                    out.write(
                        String.format("ID: %-7d Type: %-10s From: %-7d To: %-7d Parent: %-7d\n",
                            x.id(), 
                            x.type().name(),
                            x.from().id(), 
                            x.to().id(), 
                            x.parent() != null ? x.parent().id(): null)
                        );
                }
            }
            out.write("\n");
            
        } catch (NumberFormatException e) {
            out.write("Invalid number format: " + e.getMessage() + "\n");
        } catch (SQLException e) {
            out.write("Database error: " + e.getMessage() + "\n");
        }
        return true;
    }
}

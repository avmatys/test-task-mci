package com.task.mci;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.task.mci.command.Command;
import com.task.mci.command.CommandRegistry;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;

public class ReplEngine {

    private final InputSource in;
    private final OutputTarget out;
    private final CommandRegistry registry;
    private final Pattern pattern = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");

    public ReplEngine(InputSource in, OutputTarget out, CommandRegistry registry) {
        this.in = in;
        this.out = out;
        this.registry = registry;
    }

    public void run() throws IOException {
        boolean running = true;
        while (running) {
            out.write("> ");
            String line = in.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) continue;

            List<String> parts = parseArgs(line);
            if (parts.isEmpty()) continue;

            String cmdName = parts.get(0);
            String[] cmdArgs = parts.size() > 1 ? parts.subList(1, parts.size()).toArray(String[]::new) : new String[0];
            Command cmd = registry.get(cmdName);
            if (cmd == null) {
                out.write("Unknown command\n");
                continue;
            }
            running = cmd.execute(cmdArgs, in, out);
        }
    }

    private List<String> parseArgs(String line) {
        Matcher matcher = pattern.matcher(line);
        List<String> parts = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                parts.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                parts.add(matcher.group(2));
            } else {
                parts.add(matcher.group(0));
            }
        }
        return parts;
    }
}

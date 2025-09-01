package com.task.mci.command.util;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CommandArgumentParser {

    public static Map<String, String> parse(String[] args) throws ParseException {
        Map<String, String> parsedArgs = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (!arg.startsWith("-")) {
                throw new ParseException("Expected flag starting with '-', got: " + arg, i);
            }
            String flag = arg;
            String value = "true"; 
            if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                value = args[++i];
            }
            parsedArgs.put(flag, value);
        }
        return parsedArgs;
    }

}

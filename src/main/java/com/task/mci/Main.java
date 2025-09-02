package com.task.mci;

import java.io.IOException;

import com.task.mci.command.CommandInitializer;
import com.task.mci.command.CommandRegistry;
import com.task.mci.dao.util.DB;
import com.task.mci.io.ConsoleInputSource;
import com.task.mci.io.ConsoleOutputTarget;
import com.task.mci.io.FileInputSource;
import com.task.mci.io.FileOutputTarget;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;

public class Main {
    public static void main(String[] args) {
        DB.init();
        InputSource in = null;
        OutputTarget out = null;
        try {
            if (args.length >= 4 && args[0].equalsIgnoreCase("-fin") && args[2].equalsIgnoreCase("-fout")) {
                in = new FileInputSource(args[1]);
                out = new FileOutputTarget(args[3]);
            } else {
                in = new ConsoleInputSource();
                out = new ConsoleOutputTarget();
            }  
            AppContext context = new AppContext();
            CommandRegistry registry = CommandInitializer.initialize(context);
            ReplEngine repl = new ReplEngine(in, out, registry);
            repl.run();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
            DB.shutdown();
        }
    }
}
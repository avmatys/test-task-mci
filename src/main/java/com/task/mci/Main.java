package com.task.mci;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.task.mci.command.CommandInitializer;
import com.task.mci.command.CommandRegistry;
import com.task.mci.dao.util.DB;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;
import com.task.mci.io.impl.ConsoleInputSource;
import com.task.mci.io.impl.ConsoleOutputTarget;
import com.task.mci.io.impl.FileInputSource;
import com.task.mci.io.impl.FileOutputTarget;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

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
            logger.log(Level.SEVERE, "Main I/O error: {0}", e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to close resources: {0}", e.getMessage());
            }
            DB.shutdown();
        }
    }
}
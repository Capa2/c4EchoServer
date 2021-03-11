package Log;

import java.io.IOException;
import java.util.logging.*;

/* We thought that logging was part of the assignment and has therefor
 created a logging setup and started logging but after it was outlined
  we have not added the logger in the final project, just the logger
  setupclasses();*/

public class ClientLogger {

    private final static Logger logger = Logger.getLogger("global");

    public static void setupLogger() {
        // reset logmanager and set new log.level to ALL
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        // Handler for console logs
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.INFO);
        logger.addHandler(ch);

        // Handler for logging into file
        try {
            FileHandler fh = new FileHandler("MyClientLogger.log", false);// append is set to false
            fh.setLevel(Level.FINE);
            logger.addHandler(fh);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "File logger not working.", e);
        }
    }
}

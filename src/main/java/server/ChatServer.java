package server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.*;

public class ChatServer {
    private final static Logger logger = Logger.getLogger("global");

    private static void setupLogger() {
        // reset logmanager and set new log.level to ALL
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        // Handler for console logs
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.INFO);
        logger.addHandler(ch);

        // Handler for logging into file
        try {
            FileHandler fh = new FileHandler("MyLogger.log", true);// append is set to true
            fh.setLevel(Level.FINE);
            logger.addHandler(fh);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "File logger not working.", e);
        }
    }
    //Call server with arguments like this: 0.0.0.0 8088 logfile.log
    public static void main(String[] args) throws UnknownHostException {
        ChatServer.setupLogger();

        String ip = "localhost";
        int port = 8088;
        String logFile = "log.txt";  //Do we need this
        try {
            if (args.length == 3) {
                ip = args[0];
                port = Integer.parseInt(args[1]);
                logFile = args[2];
          //  } else {
                throw new IllegalArgumentException("Server not provided with the right arguments");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }
        Server server = new Server(port);
    }

}

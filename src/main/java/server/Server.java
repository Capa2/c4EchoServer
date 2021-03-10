package server;

import server.connection.*;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    final private Vector<Session> sessions;
    final private ExecutorService executor; // thread pool

    private final static Logger logger = Logger.getLogger("global");


    public Server(int port) {
        sessions = new Vector<>(); // thread safe storage of client sockets
        executor = Executors.newFixedThreadPool(2);
        executor.submit(new Listener(sessions, port)); // scans for new clients
        executor.submit(new IoProtocol(sessions)); // handles input and output streams
        logger.log(Level.INFO, "Server Online");
        //System.out.println("Server online");
    }
}

package server;

import server.connection.*;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    final private Vector<Session> sessions;
    final private ConcurrentHashMap<String, Boolean> users;
    final private ExecutorService executor; // thread pool

    public Server(int port) {
        sessions = new Vector<>(); // thread safe storage of client sockets
        users = new ConcurrentHashMap<>(); // thread safe storage of client info
        addSampleUsers();

        executor = Executors.newFixedThreadPool(2);
        executor.submit(new Listener(sessions, port)); // scans for new clients
        executor.submit(new IoProtocol(sessions, users)); // handles input and output streams
        System.out.println("Server online");
    }

    private void addSampleUsers() {
        System.out.println("Adding sample users..");
        users.put("Johan", false);
        users.put("Jens", false);
        users.put("Alex", false);
    }
}

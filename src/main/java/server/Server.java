package server;

import server.connection.*;
import java.net.Socket;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    final private Map<String, Boolean> users;
    final private Vector<Socket> connections;
    final private ExecutorService executor; // thread pool

    public Server(int port) {
        users = new ConcurrentHashMap<>(); // thread safe storage of client info
        connections = new Vector<>(); // thread safe storage of client sockets
        executor = Executors.newFixedThreadPool(3);
        executor.submit(new Listener(connections, port)); // scans for new connections and put them in connections vector
        executor.submit(new InputScanner(connections)); // scans for data received from clients
        executor.submit(new ResponseProtocol(connections)); // send data back to client
    }
}

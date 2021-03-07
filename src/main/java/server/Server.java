package server;

import server.connection.*;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    Vector<Socket> connections;
    ExecutorService executor; // thread pool

    public Server(int port) {
        connections = new Vector<Socket>(); // thread safe storage of client sockets
        executor = Executors.newFixedThreadPool(3);
        executor.submit(new Listener(connections, port)); // scans for new connections and put them in connections vector
        executor.submit(new InputScanner(connections)); // scans for data received from clients
        executor.submit(new ResponseProtocol(connections)); // send data back to client
    }
}

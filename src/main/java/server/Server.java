package server;

import server.connection.*;
import server.io.In;
import server.io.Out;
import server.scan.ClientScan;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    //final private Vector<Session> sessions;
    final private ConcurrentHashMap<String, String> userMap;
    final private Vector<In> ins;
    final private Vector<Out> outs;
    final private ExecutorService executor; // thread pool
    public Server(int port) {
        ins = new Vector<>();
        outs = new Vector<>();
        executor = Executors.newFixedThreadPool(2);
        executor.submit(new ClientScan(sessions, port)); // scans for new clients
        executor.submit(new IoProtocol(sessions)); // handles input and output streams
        System.out.println("Server online");
        userMap = new ConcurrentHashMap<>();
        userMap.put("Johan", "free");
        userMap.put("Jens", "free");
        userMap.put("Alex", "free");

    }
}

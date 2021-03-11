package server;

import server.io.In;
import server.io.Out;
import server.protocols.Entity;
import server.protocols.PullTranslator;
import server.scan.ClientScan;
import server.scan.PullScan;

import java.util.Vector;
import java.util.concurrent.*;

public class Server {
    final private ConcurrentHashMap<String, String> userMap;
    final private Vector<In> ins;
    final private Vector<Out> outs;
    final private BlockingQueue<String> pullQueue;
    final private BlockingQueue<Entity> entities;
    final private ExecutorService executor;

    public Server(int port) {
        this.userMap = new ConcurrentHashMap<>();
        this.userMap.put("Johan", "free");
        this.userMap.put("Jens", "free");
        this.userMap.put("Alex", "free");

        this.ins = new Vector<>();
        this.outs = new Vector<>();
        this.pullQueue = new ArrayBlockingQueue<>(100);
        this.entities = new ArrayBlockingQueue<>(100);

        executor = Executors.newFixedThreadPool(5);
        executor.submit(new ClientScan(ins, outs, port));
        executor.submit(new PullScan(ins));
        executor.submit(new PullTranslator(userMap, pullQueue));
        System.out.println("Server online");

    }
}

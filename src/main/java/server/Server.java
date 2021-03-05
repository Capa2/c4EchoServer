package server;

import server.connection.Listener;
import java.net.Socket;
import java.util.Vector;

public class Server {
    Vector<Socket> connections;
    Listener incomming;
    public Server(int port) {
        connections = new Vector<Socket>();
        incomming = new Listener(connections, port);
    }
}

package server;

import server.connection.Listener;
import server.connection.ResponseProtocol;

import java.net.Socket;
import java.util.Vector;

public class Server {
    Vector<Socket> connections;
    Listener incomming;
    ResponseProtocol responseProtocol;
    public Server(int port) {
        connections = new Vector<Socket>(); // thread safe storage of client sockets
        incomming = new Listener(connections, port); // scans for new connections and put them in connections vector
        responseProtocol = new ResponseProtocol(connections); // send data back to connections
    }
    // decision making processes should possibly happen here, or in a new class. the three classes above should have a singular purpose.
}

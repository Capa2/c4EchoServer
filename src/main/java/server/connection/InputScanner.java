package server.connection;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class InputScanner implements Runnable, Closeable {
    Vector<Socket> connections;
    boolean open;

    public InputScanner(Vector<Socket> connections) {
        this.connections = connections;
        open = false;
    }

    @Override
    public void run() {
        while (open) {
            scan(connections);
        }
    }

    @Override
    public void close() {
        open = false;
    }

    private void scan(Vector<Socket> sockets) {
        sockets.forEach((socket) -> {
                    try {
                        if (socket.getInputStream().available() > 0) {
                        // what to do if a client has sent data to the server...  send to Server class? responseprotocol?
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ); // forEach end
    }
}

package server.connection;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class InputScanner implements Runnable, Closeable {
    Vector<Socket> connections;

    public InputScanner(Vector<Socket> connections) {
        this.connections = connections;
    }

    @Override
    public void close() throws IOException {
        
    }

    @Override
    public void run() {

    }
}

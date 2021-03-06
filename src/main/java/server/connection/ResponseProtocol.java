package server.connection;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ResponseProtocol implements Runnable, Closeable {
    Vector<Socket> connections;

    public ResponseProtocol(Vector<Socket> connections) {
        this.connections = connections;
    }

    @Override
    public void run() {

    }

    @Override
    public void close() throws IOException {

    }


}

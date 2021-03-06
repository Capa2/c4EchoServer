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
    public void run() {
        //while (????) {
            scan(connections);
        //}
    }

    @Override
    public void close() throws IOException {

    }

    private void scan(Vector<Socket> sockets) {
        sockets.forEach((socket) -> {
                    try {
                        if (socket.getInputStream().available() > 0) {
                        // what to do if a client has sent data to the server
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ); // forEach end
    }
}

package server.connection;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

public class InputScanner implements Runnable, Closeable {
    final private Vector<Socket> connections;
    private boolean open;

    public InputScanner(Vector<Socket> connections) {
        this.connections = connections;
        open = false;
    }

    @Override
    public void run() {
        open = true;
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
                        if (socket.getInputStream().available() > 8) {
                            String input = new DataInputStream(new BufferedInputStream(socket.getInputStream())).readUTF();
                            // notifyAll();
                        // what to do if a client has sent data to the server...  send to Server class? responseprotocol?
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ); // forEach end
    }
}

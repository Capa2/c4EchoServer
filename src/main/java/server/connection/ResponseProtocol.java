package server.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ResponseProtocol implements Runnable {
    final private Vector<Socket> connections;

    public ResponseProtocol(Vector<Socket> connections) {
        this.connections = connections;
    }

    @Override
    public void run() {
    }

    private void push(Socket socket, String cmd) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void online() {

    }

    private void message() {

    }

    private void close() {

    }
}

package server.connection;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Listener implements Runnable, Closeable {
    ServerSocket serverSocket;
    Vector<Socket> connections;

    public Listener(Vector<Socket> sockets, int port) {
        this.connections = sockets;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void listen(ServerSocket serverSocket) {
        Socket newClient = null;
        try {
            newClient = this.serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(newClient.isConnected()) connections.add(newClient);
        }
    }

    @Override
    public void run() {
        while (isOnline()) {
            listen(serverSocket);
        }
    }

    @Override
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Listener terminated.");
        }
    }

    public boolean isOnline() {
        return !serverSocket.isClosed();
    }
}

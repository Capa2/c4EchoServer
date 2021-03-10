package server.connection;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Listener implements Runnable, Closeable {
    private ServerSocket serverSocket;
    final private Vector<Session> sessions;

    public Listener(Vector<Session> sessions, int port) {
        this.sessions = sessions;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen(ServerSocket serverSocket) {
        Socket s = null;
        try {
            s = this.serverSocket.accept();
            System.out.println("Client connected from " + serverSocket.getLocalSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
        if (s.isConnected()) sessions.add(new Session(s));

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
        }
        System.out.println("Listener terminated.");

    }

    public boolean isOnline() {
        return !serverSocket.isClosed();
    }
}

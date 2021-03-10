package server.connection;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Listener implements Runnable, Closeable {
    private ServerSocket serverSocket;
    final private Vector<Session> sessions;

    private final static Logger logger = Logger.getLogger("global");

    public Listener(Vector<Session> sessions, int port) {
        this.sessions = sessions;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            close();
            logger.log(Level.WARNING, "Error creating a new ServerSocket//Stream Closed", e);
        }
    }

    private void listen(ServerSocket serverSocket) {
        Socket s = null;
        try {
            s = this.serverSocket.accept();
            logger.log(Level.INFO, "Client connected from " + s.getLocalSocketAddress());
            //System.out.println("Client connected from " + s.getLocalSocketAddress());
        } catch (IOException e) {
            //e.printStackTrace();
            logger.log(Level.WARNING, "Error in accepting the newClient", e);
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
            logger.log(Level.SEVERE, "ServerSocket could not be closed successfully", e);
            //e.printStackTrace();
        } finally {
            logger.log(Level.INFO, "Serversocket closed.");
            //System.out.println("Serversocket closed.");
        }
    }

    public boolean isOnline() {
        return !serverSocket.isClosed();
    }
}

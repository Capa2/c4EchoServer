package server.connection;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session implements Closeable {
    final private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String user;
    private boolean isClosed;

    private final static Logger logger = Logger.getLogger("global");

    public Session(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error in creating DataInput-/DataOutputStream for socket : " + this.socket, e);
            //e.printStackTrace();
        }
    }

    public void push(String string) {
        try {
            out.writeUTF(string);
            //logger.log(Level.FINE, "OUT@" + socket.getLocalSocketAddress() + ": " + string);
            //System.out.println("OUT@" + socket.getLocalSocketAddress() + ": " + string);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error in writing", e);
            //e.printStackTrace();
        } finally {
            if (string.startsWith("CLOSE#")) close();
            logger.log(Level.INFO, "Closed the stream: CLOSE#, succesfully");
        }
    }

    public String pull() {
        try {
            String input = in.readUTF();
            System.out.println("IN@" + socket.getLocalSocketAddress() + ": " + input);
            return input;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error in reading", e);
            //e.printStackTrace();
        }
        return null;
    }

    public boolean hasIncomingData() {
        int bytes = 0;
        try {
            bytes = in.available();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error in reading available incomming data", e);
            //e.printStackTrace();
        }
        return bytes > 0;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void close() {
        try {
            System.out.println("Closing connection from " + socket.getLocalSocketAddress());
            socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error in closing connection", e);
            //e.printStackTrace();
        } finally {
            isClosed = true;
        }
    }
}

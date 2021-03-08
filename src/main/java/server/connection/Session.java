package server.connection;

import java.io.*;
import java.net.Socket;

public class Session implements Closeable {
    final private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String user;

    public Session(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void push(String string) {
        try {
            out.writeUTF(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String pull() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasIncomingData() {
        int bytes = 0;
        try {
            bytes = in.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes > 0;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }


    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

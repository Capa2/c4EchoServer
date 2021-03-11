package server.connection;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

public class Session implements Closeable {
    final private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Session(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void push(String string) {
        try {
            out.writeUTF(string);
            System.out.println("OUT@" + getAddress() + ": " + string);
        } catch (IOException e) {
            e.printStackTrace();
            close();
        } finally {
            if (string.startsWith("CLOSE#")) close();
        }
    }

    public String pull() {
        try {
            String pull = in.readUTF();
            System.out.println("IN@" + getAddress() + ": " + pull);
            return pull;
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
        return null;
    }

    public boolean hasIncomingData() {
        int bytes = 0;
        try {
            bytes = in.available();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
        return bytes > 0;
    }

    public String getAddress() {
        return socket.getRemoteSocketAddress().toString();
    }

    public void close() {
        try {
            out.flush();
            System.out.println("Closing connection from " + socket.getLocalSocketAddress());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

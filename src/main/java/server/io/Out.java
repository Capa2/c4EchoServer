package server.io;

import java.io.*;
import java.net.Socket;

public class Out {
    private String address;
    private DataOutputStream out;
    private boolean isConnected;

    public Out(Socket socket) {
        try {
            address = socket.getRemoteSocketAddress().toString();
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            isConnected = false;
        }
        if (socket.isConnected()) isConnected = true;
    }

    public void push(String string) {
        if (isConnected()) try {
            out.writeUTF(string);
        } catch (IOException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    public String getAddress() {
        return address;
    }

    public boolean isConnected() {
        return isConnected;
    }
}

package server.io;

import java.io.*;
import java.net.Socket;

public class In {
    private String address;
    private DataInputStream input;
    private boolean isConnected;

    public In(Socket socket) {
        try {
            address = socket.getRemoteSocketAddress().toString();
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            isConnected = false;
        }
        if (socket.isConnected()) isConnected = true;
    }

    public String pull() {
        if (isConnected && hasIncomingData()) try {
            return input.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            isConnected = false;
        }
        return null;
    }

    public boolean hasIncomingData() {
        int bytes = 0;
        if (isConnected()) try {
            bytes = input.available();
        } catch (IOException e) {
            e.printStackTrace();
            isConnected = false;
        }
        return bytes > 0;
    }

    public String getAddress() {
        return address;
    }

    public boolean isConnected() {
        return isConnected;
    }
}

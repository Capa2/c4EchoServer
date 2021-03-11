package server.scan;

import server.io.In;
import server.io.Out;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ClientScan implements Runnable, Closeable {
    private ServerSocket serverSocket;
    private Vector<In> ins;
    private Vector<Out> outs;

    public ClientScan(Vector<In> ins, Vector<Out> outs, int port) {
        this.ins = ins;
        this.outs = outs;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    private void listen(ServerSocket serverSocket) {
        try {
            Socket c = serverSocket.accept();
            storeIo(c);
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    private synchronized void storeIo(Socket c) {
        ins.add(new In(c));
        outs.add(new Out(c));
    }

    public boolean isOnline() {
        return !serverSocket.isClosed();
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
    }
}

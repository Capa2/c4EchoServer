package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    final static int ServerPort = 8088;

    public static void main(String args[]) throws UnknownHostException, IOException {

        Scanner scn = new Scanner(System.in);

        InetAddress ip = InetAddress.getByName("karpantschof.com");

        Socket s = new Socket(ip, ServerPort);

        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    String msg = scn.nextLine();

                    try {
                        dos.writeUTF(msg);

                    } catch (IOException e) {
                        try {
                            s.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        System.out.println("Closed");
                        break;
                    }
                }
            }

        });


        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        String msg = dis.readUTF();
                        System.out.println(msg);
                        if (msg.contains("CLOSE#0")) {
                            System.out.println("Your connection has been closed");
                        }
                        else if (msg.contains("CLOSE#1")) {
                            System.out.println("Illegal input");
                        }
                        else if (msg.contains("CLOSE#2")) {
                            System.out.println("Invalid Username");
                        }
                    } catch (IOException e) {
                        try {
                            s.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        System.out.println("Closed");
                        break;
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }


}

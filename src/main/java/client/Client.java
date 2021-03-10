package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.*;

public class Client {

    final static int ServerPort = 8088;

    private final static Logger logger = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    private static void setupLogger() {
        // reset logmanager and set new log.level to ALL
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        // Handler for console logs
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.SEVERE);
        logger.addHandler(ch);

        // Handler for logging into file
        try {
            FileHandler fh = new FileHandler("MyLogger.xml", true);// append is set to true
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.FINE);
            logger.addHandler(fh);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "File logger not working.", e);
        }
    }

    public static void main(String args[]) throws UnknownHostException, IOException
    {

        Scanner scn = new Scanner(System.in);

        InetAddress ip = InetAddress.getByName("karpantschof.com");

        Socket s = new Socket(ip, ServerPort);

        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    String msg = scn.nextLine();

                    try {

                         dos.writeUTF(msg);
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Failed to write", e);
                        //e.printStackTrace();
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
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Failed to read message", e);
                        //e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }


}

package server;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Vector;

public class ChatServer {
    //Call server with arguments like this: 0.0.0.0 8088 logfile.log
    public static void main(String[] args) throws UnknownHostException {
        String ip = "localhost";
        int port = 8088;
        String logFile = "log.txt";  //Do we need this
        try {
            if (args.length == 3) {
                ip = args[0];
                port = Integer.parseInt(args[1]);
                logFile = args[2];
            } else {
                throw new IllegalArgumentException("Server not provided with the right arguments");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }

        Server server = new Server(port);
    }


}

package server.connection;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class IoProtocol implements Runnable, Closeable {
    final private Vector<Session> sessions;
    final private String[] validNames;
    private boolean open;

    public IoProtocol(Vector<Session> sessions) {
        validNames = new String[] {"Johan", "Jens", "Alex"};
        this.sessions = sessions;
        open = false;
    }

    private void scan(Vector<Session> sessions) {
        for (Session ses : sessions) {
            if (ses.isClosed()) continue;
            if (ses.hasIncomingData()) handleInput(ses, ses.pull());
        }
    }

    // PROTOCOL START
    private void handleInput(Session ses, String input) {
        StringTokenizer tokenizer = new StringTokenizer(input, "#");
        int tokenCount = tokenizer.countTokens();
        if (tokenCount <= 0 || tokenCount >= 4) {
            ses.push("CLOSE#1");
            return;
        }
        String token = tokenizer.nextToken();
        // CLOSE PROTOCOL (CLIENT SIDE)
        if (token.equals("CLOSE")) {
            ses.push((tokenCount == 1) ? "CLOSE#0" : "CLOSE#1");
        }
        // CONNECT PROTOCOL
        else if (ses.getUser() == null) {
            if (token.equals("CONNECT")) {
                String myNameIs = tokenizer.nextToken();
                for (String validName : validNames) {
                    if (myNameIs.equals(validName)) {
                        ses.setUser(validName);
                        for (Session s : sessions) if(s.getUser() != null) s.push(getOnlineString());
                        break;
                    }
                }
                if (ses.getUser() == null) ses.push("CLOSE#2"); // user not found

            } else ses.push("CLOSE#1"); // unassigned user didn't call CONNECT
        }
        // SEND PROTOCOL
        else if (token.equals("SEND")) {

            String[] rx = new String[0]; // String array for storing handles for multiple recipients

            if (tokenCount == 3) {
                String receiver = tokenizer.nextToken();
                if(receiver.contains(",")){
                    rx = receiver.split(","); // seperates usernames in case of multiple recipients
                }
                String message = tokenizer.nextToken();
                for (Session s : sessions) {
                    for(int i = 0; i <= rx.length-1; i++){  // sends message to each recipient in case of multiple recipients
                        if (s.getUser() == null || !rx[i].equals(s.getUser()) && !rx[i].equals("*")) continue;
                        s.push("MESSAGE#" + message);
                    }
                    if (s.getUser() == null || !receiver.equals(s.getUser()) && !receiver.equals("*")) continue;
                    s.push("MESSAGE#" + message);
                }
            } else ses.push("CLOSE#1"); // wrong amount of tokens for message
        } else ses.push("CLOSE#1"); // user didn't call CONNECT, SEND or CLOSE
    } // PROTOCOL END

    private String getOnlineString() {
        StringBuilder onlineUsers = new StringBuilder("ONLINE#");
        for (Session s : sessions) if (s.getUser() != null) {
                onlineUsers.append(s.getUser());
                onlineUsers.append(",");
        }
        onlineUsers.deleteCharAt(onlineUsers.lastIndexOf(","));
        return onlineUsers.toString();
    }

    @Override
    public void run() {
        open = true;
        while (open) scan(sessions);
    }

    @Override
    public void close() {open = false;}
}

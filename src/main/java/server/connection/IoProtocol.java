package server.connection;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class IoProtocol implements Runnable, Closeable {
    final private Vector<Session> sessions;
    final private String[] validNames;
    private boolean open;

    public IoProtocol(Vector<Session> sessions) {
        validNames = new String[]{"Johan", "Jens", "Alex"};
        this.sessions = sessions;
        open = false;
    }

    private void scan(Vector<Session> sessions) {
        while (open) {
            sessions.forEach(ses -> {
                if (ses.hasIncomingData() && !ses.isClosed()) handleInput(ses, ses.pull());
            });
        }
    }

    // PROTOCOL START
    private synchronized void handleInput(Session ses, String input) {
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
                        sessions.forEach(s -> {if (s.getUser() != null) s.push(getOnlineString());});
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
                String message = tokenizer.nextToken();

                if (receiver.contains(",")) {
                    rx = receiver.split(","); // separates usernames in case of multiple recipients
                }
                String[] finalRx = rx;
                sessions.forEach(s -> {
                  for (int i = 0; i <= finalRx.length - 1; i++) {  // sends message to each recipient in case of multiple recipients
                        if (s.getUser() == null || !finalRx[i].equals(s.getUser()) && !finalRx[i].equals("*")) continue;
                        s.push("MESSAGE#" + message);
                    }
                    if (s.getUser() != null && receiver.equals(s.getUser()) || receiver.equals("*")) s.push("MESSAGE#" + message);
                });
            } else ses.push("CLOSE#1"); // wrong amount of tokens for message
        } else ses.push("CLOSE#1"); // user didn't call CONNECT, SEND or CLOSE
    } // PROTOCOL END

    private synchronized String getOnlineString() {
        StringBuilder onlineUsers = new StringBuilder("ONLINE#");
        sessions.forEach(s -> {

            int usernameLength = 0;

            if (s.getUser() != null) {
                onlineUsers.append(s.getUser());
                onlineUsers.append(",");
                usernameLength = s.getUser().length();

            }
            if (s.isClosed() == true) {
                onlineUsers.delete(onlineUsers.length()-usernameLength-1, onlineUsers.length()-1);
                System.out.println(s.getUser());
                onlineUsers.deleteCharAt(onlineUsers.lastIndexOf(","));


                //deletes last logged in user when someone logs off.
                //fix this so it deletes the correct user.
            }
        });
        onlineUsers.deleteCharAt(onlineUsers.lastIndexOf(","));
        System.out.println(onlineUsers);
        return onlineUsers.toString();
    }

    @Override
    public void run() {
        open = true;
        scan(sessions);
    }

    @Override
    public void close() {
        open = false;
    }
}

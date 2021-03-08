package server.connection;

import java.io.*;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class IoProtocol implements Runnable, Closeable {
    final private Vector<Session> sessions;
    final private Map<String, Boolean> users;
    private boolean open;

    public IoProtocol(Vector<Session> sessions, ConcurrentHashMap<String, Boolean> users) {
        this.users = users;
        this.sessions = sessions;
        open = false;
    }

    private void scan(Vector<Session> sessions) {
        sessions.forEach((ses) -> {
            if (ses.hasIncomingData()) handleInput(ses, ses.pull());
        });
    }

    private void handleInput(Session ses, String input) {
        StringTokenizer tokenizer = new StringTokenizer(input, "#");
        int tokenCount = tokenizer.countTokens();
        if (tokenCount <= 0 || tokenCount >= 4) {
            ses.push("CLOSE#1");
            return;
        }
        String token = tokenizer.nextToken();
        if (token.equals("CLOSE")) {
            ses.push((tokenCount == 1) ? "CLOSE#0" : "CLOSE#1");
        } else if (ses.getUser() == null) {
            if (token.equals("CONNECT")) {
                String myNameIs = tokenizer.nextToken();
                for (Map.Entry entry : users.entrySet()) {
                    if (myNameIs.equals(entry.getKey())) {
                        ses.setUser(myNameIs);
                        users.put(myNameIs, true); // true = online;
                        ses.push(getOnlineUserString());
                        break;
                    }
                }
                if (ses.getUser() == null) ses.push("CLOSE#2"); // user not found

            } else ses.push("CLOSE#1"); // unassigned user didn't call CONNECT
        } else if (token.equals("SEND")) {
            if (tokenCount == 3) {
                String receiver = tokenizer.nextToken();
                sessions.forEach((session) -> {
                    if (session.getUser() != null) {
                        if (receiver.equals(session.getUser()) || receiver.equals("*")) {
                            session.push("MESSAGE#" + tokenizer.nextToken());
                        }
                    }
                });
            } else ses.push("CLOSE#1"); // wrong amount of tokens for message
        } else ses.push("CLOSE#1"); // user didn't call CONNECT, SEND or CLOSE
    }

    private String getOnlineUserString() {
        StringBuilder onlineUsers = new StringBuilder("ONLINE#");
        users.forEach((key, value) -> {
            if (value) {
                onlineUsers.append(key);
                onlineUsers.append(",");
            }
        });
        onlineUsers.deleteCharAt(onlineUsers.lastIndexOf(","));
        return onlineUsers.toString();
    }

    @Override
    public void run() {
        open = true;
        while (open) {
            scan(sessions);
        }
    }

    @Override
    public void close() {
        open = false;
    }
}

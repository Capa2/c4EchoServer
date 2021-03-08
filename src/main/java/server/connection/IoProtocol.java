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
        if (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (ses.getUser() == null) {
                if (token.equals("CONNECT")) {
                    String myNameIs = tokenizer.nextToken();
                    users.forEach((key, value) -> {
                        if (myNameIs.equals(key)) {
                            ses.setUser(myNameIs);
                            users.put(myNameIs, true); // true = online;
                            ses.push(getOnlineUserString());
                        }
                    });
                } else {
                    ses.push("CLOSE#2");
                }
            }
        } else ses.push("CLOSE#1"); // no tokens: illegal input
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

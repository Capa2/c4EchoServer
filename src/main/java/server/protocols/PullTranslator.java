package server.protocols;

import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class PullTranslator implements Runnable {
    final private ConcurrentHashMap<String, String> userMap;
    final private BlockingQueue<String> pullQueue;

    public PullTranslator(ConcurrentHashMap<String, String> userMap, BlockingQueue<String> pullQueue) {
        this.userMap = userMap;
        this.pullQueue = pullQueue;
    }

    private void translate() {

    }

    @Override
    public void run() {
        while (true) {
            if (pullQueue.isEmpty()) continue;
            Entity e = createEntity(pullQueue.poll());
        }
    }

    private Entity createEntity(String pull) {
        StringTokenizer tok = new StringTokenizer(Objects.requireNonNull(pullQueue.poll()), "#");
        Entity e = new Entity();
        e.setSenderAddress(tok.nextToken());
        e.setCommand(tok.nextToken());
        switch (e.getCommand()) {
            case "CONNECT":
                e.setContent(tok.nextToken());
            case "SEND":
                for (String r : tok.nextToken().split(",")) e.addRecipientAddress(userMap.get(r));
                e.setContent(tok.nextToken());
        }
        return e;
    }
}

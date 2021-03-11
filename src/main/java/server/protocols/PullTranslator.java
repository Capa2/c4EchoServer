package server.protocols;

import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class PullTranslator implements Runnable {
    final private ConcurrentHashMap<String, String> userMap;
    final private BlockingQueue<String> pullQueue;
    final private BlockingQueue<Entity> entities;

    public PullTranslator(ConcurrentHashMap<String, String> userMap, BlockingQueue<String> pullQueue) {
        this.userMap = userMap;
        this.pullQueue = pullQueue;
        this.entities = new ArrayBlockingQueue<Entity>(100);
    }

    @Override
    public void run() {
        while (true) {
            if (pullQueue.isEmpty()) continue;
            try {
                entities.put(createEntity(pullQueue.poll()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Entity createEntity(String constructor) {
        StringTokenizer tok = new StringTokenizer(constructor, "#");
        Entity e = new Entity();
        e.setSenderAddress(tok.nextToken());
        e.setCommand(tok.nextToken());
        switch (e.getCommand()) {
            case "CLOSE":
                e.setCloseCode(0);
                break;
            case "CONNECT":
                if (tok.hasMoreTokens()) e.setContent(tok.nextToken());
                else e.setCloseCode(1);
                break;
            case "SEND":
                for (String r : tok.nextToken().split(",")) {
                    if (r.equals("*")) e.setRecipientsAddresses(userMap.values());
                    else if (userMap.containsKey(r)) e.addRecipientAddress(userMap.get(r));
                    else e.setCloseCode(2);
                }
                if (tok.hasMoreTokens()) e.setContent(tok.nextToken());
                else e.setCloseCode(1);
                break;
            default:
                e.setCloseCode(1);
        }
        return e;
    }
}

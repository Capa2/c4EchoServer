package server.protocols;

import java.util.concurrent.BlockingQueue;

public class EntityHandler {
    final private BlockingQueue<Entity> entities;

    public EntityHandler(BlockingQueue<Entity> entities) {
        this.entities = entities;
    }
}

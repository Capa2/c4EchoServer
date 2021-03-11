package server.scan;

import server.io.In;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PullScan implements Runnable {
    final private Vector<In> ins;
    final private BlockingQueue<String> pullQueue;

    public PullScan(Vector<In> ins) {
        this.ins = ins;
        this.pullQueue = new ArrayBlockingQueue<String>(100);
    }

    private void scan(Vector<In> ins) {
        ins.parallelStream().forEach(in -> {
            String pull = in.pull();
            if (pull != null) {
                try {
                    pullQueue.put(in.getAddress() + "#" + pull);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public BlockingQueue<String> getPullQueue() {
        return pullQueue;
    }

    @Override
    public void run() {
        while (true) {
            scan(ins);
        }
    }
}

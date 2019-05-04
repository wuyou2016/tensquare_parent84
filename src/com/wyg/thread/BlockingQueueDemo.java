package com.wyg.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueDemo {
    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue=new ArrayBlockingQueue<>(3);

        try {
            blockingQueue.put("a");
            blockingQueue.put("b");
            blockingQueue.put("c");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package main.com.pos.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainThread extends Thread {

    private final Thread[] threads = {
        new Thread(new DatabaseConnectionTask()),
        new Thread(new RenderingGraphic()),
        new Thread(new LoginTask())
    };
    private final ExecutorService executor = Executors.newFixedThreadPool(threads.length);

    public MainThread() {

        for (Thread thread : threads) {
            System.out.println("Starting thread: " + thread.getName());
            executor.submit(thread);
        }
        
    }

    public void stopAllThreads() { executor.shutdownNow(); }
}
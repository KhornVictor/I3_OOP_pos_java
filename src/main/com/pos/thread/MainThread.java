package main.com.pos.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import main.com.pos.util.Color;


public class MainThread extends Thread {

    private final Thread[] threads = {
        new Thread(new DatabaseConnectionTask()),
        new Thread(new RenderingGraphic()),
        new Thread(new LoginTask()),
        new Thread(new DownloadImage())
    };
    private final ExecutorService executor = Executors.newFixedThreadPool(threads.length);

    public MainThread() {

        for (Thread thread : threads) {
            System.out.println( Color.YELLOW + "Starting thread: " + thread.getName() + Color.RESET);
            executor.submit(thread);
        }
        
    }

    public void stopAllThreads() { executor.shutdownNow(); }
}
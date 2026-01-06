package main.com.pos.thread;

import java.awt.EventQueue;
import main.com.pos.build.ContentBuild;


public class RenderingGraphic implements Runnable {

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        EventQueue.invokeLater(() -> {
            new ContentBuild().buildDashboard();
        });
        long endTime = System.currentTimeMillis();
        System.out.println("Rendering graphic task completed in " + (endTime - startTime) + " ms");
    }
}
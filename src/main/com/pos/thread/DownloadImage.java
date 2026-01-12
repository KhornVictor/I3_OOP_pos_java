package main.com.pos.thread;

import main.com.pos.download.DownloadProcess;

public class DownloadImage implements Runnable {

    @Override
    public void run() {
        new DownloadProcess().startDownload();
    }
}

package main.com.pos;

import main.com.pos.thread.MainThread;

public class Main {
    public static void main(String[] args) {
        new MainThread().start();
    }
}
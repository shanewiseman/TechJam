package com.akamai.techjam.distributedsystem;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: hmahadev
 */
public class SocketServerThread implements Runnable {

    private Integer portNumber;
    private Node coreNode;
    private ServerSocket listener = null;
    private boolean isStopped = false;
    private Thread runningThread = null;

    public SocketServerThread(final Integer portNumber, final Node coreNode) {
        this.portNumber = portNumber;
        this.coreNode = coreNode;
        System.out.println(coreNode.getId());
    }

    @Override
    public void run() {
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        try {
            this.listener = new ServerSocket(this.portNumber);
            while (! isStopped()) {
                final Socket socket = listener.accept();
                new Thread(new SocketWorkerRunnable(socket, coreNode)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.listener.close();
        } catch (IOException e) {
            throw new RuntimeException("Error stopping the socket server", e);
        }
    }
}

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

    public SocketServerThread(final Integer portNumber, final Node coreNode) {
        this.portNumber = portNumber;
        this.coreNode = coreNode;
        System.out.println(coreNode.getId());
    }

    @Override
    public void run() {
        ServerSocket listener;
        try {
            listener = new ServerSocket(this.portNumber);
            while (true) {
                final Socket socket = listener.accept();
                new Thread(new SocketWorkerRunnable(socket, coreNode)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.akamai.techjam.distributedsystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: hmahadev
 */
public class SocketWorkerRunnable implements Runnable {

    private Socket socket = null;
    private Node coreNode;

    public SocketWorkerRunnable(Socket socket, Node coreNode) {
        this.socket = socket;
        this.coreNode   = coreNode;
    }


    @Override
    public void run() {
        try {
            String request = "";
            final InputStream in = socket.getInputStream();
            for (int b = 0; b!=10;) {
                request += (char) b;
                b = in.read();
            }

            final String goToNode;
            if (!request.isEmpty() && request.length() <= 9) {
                goToNode = coreNode.get(request);
            } else {
                goToNode = "";
            }

            String responseString = "";
            if (goToNode.equals(coreNode.getId())) {
                if (coreNode.getCache().getIfPresent(request)!=null) {
                    responseString = "CACHE";
                } else {
                    coreNode.getCache().put(request, "CACHE");
                    responseString = "FETCH";
                }
            } else {
                responseString = "FORWARD " + goToNode;
            }

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

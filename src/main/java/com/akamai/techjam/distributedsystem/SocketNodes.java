package com.akamai.techjam.distributedsystem;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: hmahadev
 */
public class SocketNodes {

    /**
     * Runs the server node.
     */
    public static void main(String[] args) throws IOException {
        final int numOfNodes = Integer.valueOf(args[0]);
        SocketServerPool.START(numOfNodes);
    }
}



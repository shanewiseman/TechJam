package com.akamai.techjam.distributedsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hmahadev
 */
public class SocketServerPool {

    final static HashMap<String, Thread> SERVER_SOCKET_PROCS = new HashMap<String, Thread>();

    public static void START() {
        final List<Integer> portNumbers = Arrays.asList(20000, 20001, 20002);

        final Node node1 = new Node(String.valueOf(20000));
        final Node node2 = new Node(String.valueOf(20001));
        final Node node3 = new Node(String.valueOf(20002));
        node1.addNodes(new ArrayList<Node>(Arrays.asList(node2, node3)));
        node2.addNodes(new ArrayList<Node>(Arrays.asList(node1, node3)));
        node3.addNodes(new ArrayList<Node>(Arrays.asList(node2, node1)));

        final Map<String, Node> portNumberToNode = new HashMap<String, Node>();
        portNumberToNode.put("20000", node1);
        portNumberToNode.put("20001", node2);
        portNumberToNode.put("20002", node3);

        for (Integer portNumber : portNumbers) {
            final Thread t = new Thread(new SocketServerThread(portNumber,portNumberToNode.get(portNumber.toString())));
            SERVER_SOCKET_PROCS.put(portNumber.toString(), t);
            t.start();
        }
    }
}
